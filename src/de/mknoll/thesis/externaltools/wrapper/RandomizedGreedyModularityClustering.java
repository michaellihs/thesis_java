package de.mknoll.thesis.externaltools.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a wrapper for using Ovelgšnnes Randomized Greedy Modularity Clustering algorithm.
 *
 * TODO refactor this class and Newman wrapper to use abstract base class
 *
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class RandomizedGreedyModularityClustering {

	/**
	 * Holds command name for clustring algorithm
	 */
	private static String COMMAND = "rgmc";
	
	
	
	/**
	 * Holds path to clustering implementation
	 */
	private String binPath;
	
	
	
	/**
	 * Holds given arguments for running algorithm
	 */
	private HashMap<String, String> arguments;
	
	
	
	/**
	 * Holds arguments string for calling algorithm on command line
	 */
	private String argumentString;
	
	
	
	/**
	 * Holds command string for calling algorithm on command line
	 */
	private String commandString;
	
	
	
	/**
	 * Holds logger implementation
	 */
	private LoggerInterface logger;



	/**
	 * If set to true, output will be logged
	 */
	private boolean logOutput = true;
	
	
	
	/**
	 * 
	 * @param binPath
	 */
	public RandomizedGreedyModularityClustering(String binPath, LoggerInterface logger) {
		this.binPath = binPath;
		this.logger = logger;
	}
	
	
	
	/**
	 * Runs algorithm with given parameters
	 * 
	 * --file arg               input graph file
  	 * --k arg (=2)             sample size
  	 * --finalk arg (=5000)     sample size for final clustering step
  	 * --runs arg (=1)          number of runs from which to pick the best result
  	 * --ensemblesize arg (=-1) size of ensemble for ensemble algorithms (-1 = ln(#vertices))
  	 * --algorithm arg (=1)     algorithm: 1: RG, 2: CGGC_RG, 3: CGGCi_RG
  	 * --clusterfile arg        file to store the detected communities
  	 * --seed arg               seed value to initialize random number generator
  	 * 
	 * In order to set arguments, please use argument with '--' and optional value, if required.
	 * 
	 * arguments.put("--file", "/path/to/file");
	 * 
	 * @param Arguments for running algorithm implementation
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public Boolean run(HashMap<String, String> arguments) throws InterruptedException, IOException {
		Boolean success = true;
		this.arguments = arguments;
		this.init();
		success = this.runCommand();
		return success;
	}



	/**
	 * If set to true, output of algorithm will be logged
	 * 
	 * @param logOutput
	 */
	public void logOutput(boolean logOutput) {
		this.logOutput = logOutput;
	}
	
	
	
	/**
	 * Actually runs clustering implementation on command line
	 * 
	 * @return True if clustering did succeed.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private Boolean runCommand() throws InterruptedException, IOException {
		this.logger.log("Start running randomized greedy modularity clustering via command " + this.commandString);
		Runtime run = Runtime.getRuntime() ;
		Process process = run.exec(this.commandString) ;
		BufferedReader outputBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output;
		while ((output = outputBuffer.readLine()) != null) {
			if (this.logOutput) {
				this.logger.log(output);
			}
		}
		outputBuffer.close();
		
		BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		while ((output = errorBuffer.readLine()) != null) {
			this.logger.log(output);
		}
		
		process.getErrorStream().close();
		process.getOutputStream().close();
		process.getInputStream().close();
		
		this.logger.log("Finished clustering via randomized greedy modularity clustering");

		return process.waitFor()  == 0;
	}
	
	
	
	/**
	 * Initializes this wrapper with given arguments
	 */
	private void init() {
		this.createArgumentString();
		this.createCommandString();
	}
	
	
	
	/**
	 * Creates argument string from given argument hashmap
	 */
	private void createArgumentString() {
		this.argumentString = "";
		for (String parameter: this.arguments.keySet()) {
			this.argumentString += " " + parameter;
			if (this.arguments.get(parameter) != "") {
				this.argumentString += " " + this.arguments.get(parameter) + " ";
			}
		}
	}
	
	
	
	/**
	 * Creates command string like "/path/to/implementation/COMMAND argumentsString"
	 */
	private void createCommandString() {
		this.commandString = this.binPath + "/" + this.COMMAND + this.argumentString;
	}
	
}
