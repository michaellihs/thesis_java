package de.mknoll.thesis.framework.core;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.MultiInjection;
import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.reader.PostgresReader;
import de.mknoll.thesis.framework.configuration.Configuration;
import de.mknoll.thesis.framework.configuration.ConfigurationBuilder;
import de.mknoll.thesis.framework.data.TestSuiteResult;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.framework.testsuite.TestSuite;
import de.mknoll.thesis.framework.testsuite.TestSuiteBuilder;
import de.mknoll.thesis.framework.testsuite.TestSuiteRunner;



/**
 * Class implements bootstrap that is used
 * to run framework with a given test configuration.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Bootstrap {
	
	/**
	 * Holds path to configuration file
	 */
	private String configurationPath;
	
	
	
	/**
	 * Holds DI container
	 */
	private MutablePicoContainer container;
	
	
	
	/**
	 * Holds framework configuration
	 */
	private Configuration configuration;
	
	
	
	/**
	 * Holds test suite for current test-run
	 */
	private TestSuite testSuite;
	
	
	
	/**
	 * Holds test suite runner
	 */
	private TestSuiteRunner testSuiteRunner;



	/**
	 * Holds test suite result
	 */
	private TestSuiteResult testSuiteResult;



	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Constructor for bootstrap
	 * 
	 * @param configurationPath Path to framework configuration file
	 */
	public Bootstrap(String configurationPath) {
		this.configurationPath = configurationPath;
		this.init();
	}
	
	
	
	/**
	 * Runs test framework with current configuration
	 * 
	 * @return Test result of current configuration
	 */
	public void run() {
		try {
			this.testSuiteResult = this.testSuiteRunner.run(this.testSuite);
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			
			this.logger.log("Error occured when trying to run testsuite: " + e.getMessage() + "\n" + exceptionAsString);
		}
	}
	
	
	
	/**
	 * Returns test suite result
	 * 
	 * @return Test suite result
	 */
	public TestSuiteResult getTestSuiteResult() {
		return this.testSuiteResult;
	}
	
	
	
	/**
	 * Initializes bootstrap
	 */
	private void init() {
		this.initContainer();
		this.initFrameworkConfiguration();
		this.initLogger();
		this.initTestSuiteAndRunner();
	}



	/**
	 * Initializes logger
	 */
	private void initLogger() {
		this.logger = this.container.getComponent(LoggerInterface.class);
	}



	/**
	 * Initializes framework configuration for bootstrap
	 */
	private void initFrameworkConfiguration() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(this.configurationPath);
		this.container.addComponent(ConfigurationBuilder.class, configurationBuilder);
		
		this.logger = this.container.getComponent(LoggerInterface.class);
		this.configuration = configurationBuilder.buildConfiguration(); 
		// Register logger class in container (we cannot do this earlier, as we need logger class name from configuration)
		try {
			Class loggerClass = this.getClass().getClassLoader().loadClass(this.configuration.getLoggerClassName());
			this.container.addComponent(LoggerInterface.class, loggerClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.container.addComponent(Configuration.class, this.configuration);
		
		// FileManager requires configuration, so we can first add it here
		this.container.addComponent(FileManager.class);
	}
	
	
	
	/**
	 * Initializes dependency injection container
	 */
	private void initContainer() {
		this.container = new PicoBuilder()
			.withCaching()
			//.withComponentFactory(new MultiInjection()) // allows multiple types of injection in the same component
			.build();
		
		
		
		// Register components in main container
		this.container.addComponent(TestSuiteBuilder.class);
		this.container.addComponent(TestSuite.class);
		this.container.addComponent(TestSuiteRunner.class, TestSuiteRunner.class);
		this.container.addComponent(MutablePicoContainer.class, container);
		
		
		// Start lifecycle in container
		this.container.start();
	}
	
	
	
	/**
	 * Initializes testsuite for current run
	 */
	private void initTestSuiteAndRunner() {
		TestSuiteBuilder testSuiteBuilder = this.container.getComponent(TestSuiteBuilder.class);
		this.testSuite = testSuiteBuilder.buildTestSuite();
		this.testSuiteRunner = this.container.getComponent(TestSuiteRunner.class);
	}

}
