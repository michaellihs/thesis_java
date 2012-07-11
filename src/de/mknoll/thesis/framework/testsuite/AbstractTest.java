package de.mknoll.thesis.framework.testsuite;

import de.mknoll.thesis.framework.configuration.TestConfiguration;
import de.mknoll.thesis.framework.data.TestResult;

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
	
	
	
	public void shutdown() {
		// normally, nothing's to do here
	}
	
}
