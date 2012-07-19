package de.mknoll.thesis.framework.testsuite;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.reader.GraphReader;
import de.mknoll.thesis.datastructures.graph.reader.PostgresReader;
import de.mknoll.thesis.framework.configuration.TestConfiguration;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;

/**
 * Class implements a single test. 
 * 
 * A test is a collection of parameters, data, algorithm and results
 * for a test-run. 
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public abstract class AbstractTest implements Test {
	
	/**
	 * Holds logger instance
	 */
	@Inject protected LoggerInterface logger;
	
	
	
	/**
	 * Holds an instance of DI container
	 */
	@Inject private MutablePicoContainer parentContainer;
	
	
	
	/**
	 * Holds our own container instance for this test
	 */
	protected MutablePicoContainer container;
	
	
	
	/**
	 * Holds file manager
	 */
	@Inject protected FileManager fileManager;
	
	
	
	/**
	 * Holds graph reader to read graphs from different sources
	 */
	protected GraphReader graphReader;
	
	
	
	/**
	 * Holds index of test within testsuite to be identifiable within testrun
	 */
	private Integer index;
	
	

	/**
	 * Holds configuration for this test.
	 */
	protected TestConfiguration testConfiguration;
	
	
	
	/**
	 * Setter for test configuration
	 * 
	 * @param testConfiguration
	 */
	public void setTestConfiguration(TestConfiguration testConfiguration) {
		this.testConfiguration = testConfiguration;
	}
	
	
	
	/**
	 * Returns class name as string
	 */
	public String toString() {
		return getClass().getName();
	}



	@Override
	public Integer index() {
		return this.index;
	}
	
	
	
	@Override
	public void index(Integer index) {
		this.index = index;
	}
	
	
	
	/**
	 * Called, whenever test is finished
	 */
	public void shutdown() {
		// normally, nothing's to do here
	}
	
	
	
	/**
	 * Initializes test
	 */
	protected void init() {
		this.container = new PicoBuilder(this.parentContainer)
			.withCaching()
			.build();

		// We add test-wide singleton of file manager
		this.container.addComponent(FileManager.class, this.fileManager);
		
		this.container.addComponent(IdNodeMap.class, new DefaultIdNodeMap());
		this.container.addComponent(GraphReader.class, PostgresReader.class);
		
		this.graphReader = this.container.getComponent(GraphReader.class);
		
		this.fileManager.setCurrentTest(this.index(), this.getClass().getName());
	}
	
}
