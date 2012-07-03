package de.mknoll.thesis.framework.testsuite;

import de.mknoll.thesis.framework.data.TestResult;

/**
 * Class implements a test runner.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TestRunner {

	/**
	 * Runs given test
	 * 
	 * @param test Test to be run
	 * @return Test result of given test
	 * @throws Exception 
	 */
	public TestResult run(Test test) throws Exception {
		TestResult testResult = test.run();
		return testResult;
	}
	
}
