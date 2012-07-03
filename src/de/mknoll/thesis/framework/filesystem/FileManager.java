package de.mknoll.thesis.framework.filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mknoll.thesis.framework.configuration.Configuration;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements file manager that handles creation of files
 * requested for test runs.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class FileManager {
	
	/**
	 * Holds path names for results and settings
	 */
	private static final String SETTINGS_PATH = "settings/";
	private static final String RESULTS_PATH = "results/";
	
	
	
	/**
	 * Holds framework configuration to get base filesystem settings from
	 */
	private Configuration frameworkConfiguration;
	
	
	
	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Holds base directory to put all files into
	 */
	private String baseDirectory;
	
	
	
	/**
	 * Holds directory for current test run
	 */
	private String testRunDirectory;



	/**
	 * Holds index and class name of current test
	 */
	private String currentTest;
	
	
	
	/**
	 * Constructor takes framework configuration and logger as arguments
	 * 
	 * @param frameworkConfiguration
	 * @param logger
	 */
	public FileManager(Configuration frameworkConfiguration, LoggerInterface logger) {
		this.frameworkConfiguration = frameworkConfiguration;
		this.logger = logger;
		this.init();
	}
	
	
	
	/**
	 * Sets current test by given test index (number of test in configurations) and class name of test
	 * 
	 * @param testIndex
	 * @param testClassName
	 */
	public void setCurrentTest(Integer testIndex, String testClassName) {
		this.currentTest = testIndex.toString() + "_" + testClassName + "/";
		File currentTestDir = new File(this.currentTest); 
		if (!currentTestDir.exists()) {
			this.createDirectory(this.testRunDirectory + this.currentTest);
		}
	}
	
	
	
	public String getCurrentResultsPath() {
		return this.getCurrentResultsPath(true);
	}
	
	
	
	public String getCurrentResultsPath(Boolean createIfNotExists) {
		String currentResultsPath = this.testRunDirectory + this.currentTest + RESULTS_PATH;
		File currentResultsDir = new File(currentResultsPath);
		if (!currentResultsDir.exists() && createIfNotExists) {
			this.createDirectory(currentResultsPath);
		}
		return currentResultsPath; 
	}
	
	
	
	public String getCurrentSettingsPath() {
		return this.getCurrentSettingsPath(true);
	}
	
	
	
	public String getCurrentSettingsPath(Boolean createIfNotExists) {
		String currentSettingsPath = this.testRunDirectory + this.currentTest + SETTINGS_PATH;
		File currentSettingsDir = new File(currentSettingsPath);
		if (!currentSettingsDir.exists() && createIfNotExists) {
			this.createDirectory(this.getCurrentSettingsPath());
		}
		return currentSettingsPath;
	}
	
	
	
	public String getResultFilePath(String fileName) throws Exception {
		if (this.currentTest == null) {
			throw new Exception("Cannot create new result file path without having set current test!");
		}
		return this.getCurrentResultsPath() + fileName;
	}
	
	
	
	public String getSettingsFilePath(String fileName) throws Exception {
		if (this.currentTest == null) {
			throw new Exception("Cannot create new settings file writer without having set current test!");
		}
		return this.getCurrentSettingsPath() + fileName;
	}
	
	
	
	/**
	 * Creates a new file writer with given filename within current results directory.
	 * 
	 * You have to set current test before!
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public FileWriter getNewResultFileWriter(String fileName) throws Exception {
		File currentResultsDir = new File(this.getCurrentResultsPath());
		if (!currentResultsDir.exists()) {
			this.createDirectory(this.getResultFilePath(fileName));
		}
		return this.createFileWriterForPath(this.getResultFilePath(fileName));
	}


	
	/**
	 * Creates a new file writer with given filename within current settings directory.
	 * 
	 * You have to set current test before!
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public FileWriter getNewSettingsFileWriter(String fileName) throws Exception {
		File currentResultsDir = new File(this.getCurrentSettingsPath());
		if (!currentResultsDir.exists()) {
			this.createDirectory(this.getCurrentSettingsPath());
		}
		return this.createFileWriterForPath(this.getSettingsFilePath(fileName));
	}



	/**
	 * Initializes file manager
	 */
	private void init() {
		this.logger.log("Initializing file manager");
		this.checkAndSetBaseDirectory();
		this.createDirectoryForCurrentTestRun();
	}
	
	
	
	/**
	 * Checks whether base directory given in configuration exists.
	 * If not, creates it and sets it as base directory.
	 */
	private void checkAndSetBaseDirectory() {
		File file = new File(this.frameworkConfiguration.getBaseDirectory());
		if (!file.exists()) {
			this.createDirectory(this.frameworkConfiguration.getBaseDirectory());
		} 
		this.baseDirectory = this.frameworkConfiguration.getBaseDirectory();
		
	}



	/**
	 * Creates a directory for a new test run. This
	 * directory is saved in filemanager and used as 
	 * base directory for all directory operations from this time on.
	 * 
	 * @return Full path of created directory
	 */
	private void createDirectoryForCurrentTestRun() {
		String testRunSubDirName = this.createTestRunSubdirName();
		this.createDirectory(testRunSubDirName);
		this.testRunDirectory = testRunSubDirName;
	}
	
	
	
	/**
	 * Creates dir name for a testrun.
	 * @return
	 */
	private String createTestRunSubdirName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String testRunSubdirName = this.baseDirectory + dateFormat.format(date).toString() + "/"; 
		return testRunSubdirName;
	}
	
	
	
	/**
	 * Creates given directory
	 * 
	 * @param Full qualified path of directory to be created
	 * @return Full qualified path of created directory
	 */
	private String createDirectory(String directory) {
		try{
			// Create one directory
			boolean success = (new File(directory)).mkdirs();
			if (success) {
				logger.log("Directory: " + directory + " created by file manager");
			} else {
				logger.log("Could not create directory: " + directory);
			}
		} catch (Exception e){//Catch exception if any
			logger.log("Error when trying to create directory: " + directory + " with error: " + e.getMessage());
		}
		return directory;
	}
	
	
	
	/**
	 * Creates and returns new filewriter for given path
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private FileWriter createFileWriterForPath(String path) throws IOException {
		FileWriter fileWriter = new FileWriter(path);
		return fileWriter;
	}

}
