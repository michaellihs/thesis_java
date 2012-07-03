package de.mknoll.thesis.framework.testsuite;

import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.framework.data.TestSuiteResult;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a test suite runner
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TestSuiteRunner {
	
	/**
	 * Holds logger
	 */
	@Inject protected LoggerInterface logger;
	
	
	
	/**
	 * Runs given test suite
	 * 
	 * @param testSuite Test suite to be run
	 * @return Test suite result
	 * @throws Exception 
	 */
	public TestSuiteResult run(TestSuite testSuite) throws Exception {
		this.logger.log("Start running testsuite.");
		TestSuiteResult testResult = testSuite.run();
		this.logger.log("Finished running testsuite.");
		return testResult;
	}

}
