package de.mknoll.thesis.framework.testsuite;

import java.util.List;
import java.util.Map;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.annotations.Inject;
import org.picocontainer.injectors.MultiInjection;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;
import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.reader.GraphReader;
import de.mknoll.thesis.datastructures.graph.reader.PostgresReader;
import de.mknoll.thesis.framework.configuration.ConfigurationBuilder;
import de.mknoll.thesis.framework.configuration.TestConfiguration;
import de.mknoll.thesis.framework.configuration.TestSuiteConfiguration;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;

/**
 * Class implements a builder for testsuite
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TestSuiteBuilder {

	/**
	 * Holds configuration for test suite
	 */
	private TestSuiteConfiguration testSuiteConfiguration;
	
	
	/**
	 * Holds logger
	 */
	@Inject private LoggerInterface logger;
	
	
	
	/**
	 * Holds configuration builder for framework
	 */
	@Inject private ConfigurationBuilder configurationBuilder; 
	
	
	
	/**
	 * Holds DI container
	 */
	@Inject private MutablePicoContainer container;
	
	
	
	/**
	 * Builds a test suite for given configuration
	 * 
	 * @return Test suite for given configuration
	 */
	public TestSuite buildTestSuite() {
		this.testSuiteConfiguration = this.configurationBuilder.buildConfiguration().getTestSuiteConfiguration();
		
		TestSuite testSuite = this.container.getComponent(TestSuite.class);
		
		Integer testIndex = 1;
		
		FileManager fileManager = this.container.getComponent(FileManager.class);
		
		for (TestConfiguration testConfiguration : this.testSuiteConfiguration) {
			if (testConfiguration.isActivated()) {
				this.logger.log("Trying to add test class to test suite: " + testConfiguration.getTestClassName());
				try {
					ClassLoader classLoader = TestSuiteBuilder.class.getClassLoader();
					Class testClass = classLoader.loadClass(testConfiguration.getTestClassName());
					
					MutablePicoContainer localContainer = new PicoBuilder(this.container)
					.withCaching()
					.withComponentFactory(new MultiInjection())
					.build();
					
					// TODO replace this with configuration later
					localContainer.addComponent(IdNodeMap.class, new DefaultIdNodeMap());
					localContainer.addComponent(MutablePicoContainer.class, localContainer);
					localContainer.addComponent(TestConfiguration.class, testConfiguration);
					localContainer.addComponent(FileManager.class, fileManager);
					localContainer.addComponent(testClass);
					
					
					Test test = localContainer.getComponent(testClass);
					test.index(testIndex);
					
					testSuite.add(test);
				} catch (Exception e) {
					this.logger.log("An error occured when trying to add test class " + testConfiguration.getTestClassName() + " to test suite: " + e.getMessage());
					e.printStackTrace();
				}
				testIndex++;
			}
		}
		
		return testSuite;
		
	}
	
}
