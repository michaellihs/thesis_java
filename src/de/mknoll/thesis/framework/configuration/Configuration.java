package de.mknoll.thesis.framework.configuration;



/**
 * Class implements framework configuration
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Configuration {
	
	/**
	 * Holds test suite configuration
	 */
	private TestSuiteConfiguration testSuiteConfiguration;
	
	
	
	/**
	 * Holds class name for logger
	 */
	private String loggerClass;



	/**
	 * Holds base directory for putting files into
	 */
	private String baseDirectory;

	
	
	/**
	 * Returns test suite configuration
	 * 
	 * @return Test suite configuration
	 */
	public TestSuiteConfiguration getTestSuiteConfiguration() {
		return this.testSuiteConfiguration;
	}
	
	
	
	/**
	 * Returns class name for logger class
	 * 
	 * @return Class name for logger class
	 */
	public String getLoggerClassName() {
		return this.loggerClass;
	}
	
	
	
	/**
	 * Getter for base directory
	 * 
	 * @return Base directory to put generated files into
	 */
	public String getBaseDirectory() {
		return this.baseDirectory;
	}



	/**
	 * Setter test suite configuration
	 * 
	 * @param testSuiteConfiguration
	 */
	public void setTestSuiteConfiguration(TestSuiteConfiguration testSuiteConfiguration) {
		this.testSuiteConfiguration = testSuiteConfiguration;
	}



	/**
	 * Setter for logger class name
	 * 
	 * @param loggerClass
	 */
	public void setLoggerClass(String loggerClass) {
		this.loggerClass = loggerClass;
	}



	/**
	 * Setter for base directory
	 * 
	 * @param baseDirectory
	 */
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
}
