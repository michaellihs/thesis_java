package de.mknoll.thesis.framework.testsuite;

import de.mknoll.thesis.framework.configuration.TestConfiguration;
import de.mknoll.thesis.framework.data.TestResult;



/**
 * Interface for tests.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface Test {

	/**
	 * Runs test.
	 * @throws Exception 
	 */
	public TestResult run() throws Exception;
	
	
	
	/**
	 * Is called, when test has finished and testsuite shuts down
	 */
	public void shutdown();
	
	
	
	/**
	 * Getter for index of test within testsuite.
	 * 
	 * Each test needs to have an index to be identifiable within a testrun.
	 */
	public Integer index();



	/**
	 * Setter for index
	 * 
	 * TODO this has a strange name due to DI. Fix this!
	 * 
	 * @param index
	 */
	void index(Integer index);
	
}
