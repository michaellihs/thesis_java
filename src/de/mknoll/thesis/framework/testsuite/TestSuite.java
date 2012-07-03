package de.mknoll.thesis.framework.testsuite;

import java.util.*;

import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.framework.data.TestResult;
import de.mknoll.thesis.framework.data.TestSuiteResult;
import de.mknoll.thesis.framework.logger.LoggerInterface;

/**
 * Class implements a test suite which is a collection
 * of tests that can be run in a single framework run.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TestSuite extends ArrayList<Test> {

	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Holds instance of logger
	 */
	@Inject private LoggerInterface logger;
	
	
	
	/**
	 * Runs tests included in this test suite
	 * 
	 * @return Test results of test suite
	 * @throws Exception 
	 */
	public TestSuiteResult run() throws Exception {
		Iterator it = this.iterator();
		TestSuiteResult testSuiteResult = new TestSuiteResult();
		while (it.hasNext()){
			Test test = (Test) it.next();
			this.logger.log("Running test " + test.toString());
			TestResult testResult = test.run();
			testSuiteResult.add(testResult);
			this.logger.log("Finished running test " + test.toString());
		}
		return testSuiteResult;
	}

}
