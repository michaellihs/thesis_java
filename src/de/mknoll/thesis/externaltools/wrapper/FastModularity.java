package de.mknoll.thesis.externaltools.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a wrapper for using Clauset's and Newman's Fast Modularity clustering algorithm.
 * Further descriptions of this algorithm can be found here:
 * 
 * http://cs.unm.edu/~aaron/research/fastmodularity.htm
 *
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class FastModularity {

	/**
	 * Holds command name for clustring algorithm
	 */
	private static String COMMAND = "FastCommunityMH";
	
	
	
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
	public FastModularity(String binPath, LoggerInterface logger) {
		this.binPath = binPath;
		this.logger = logger;
	}
	
	
	
	/**
	 * Runs algorithm with given parameters
	 * 
	 * -f <filename>	give the target .pairs file to be processed
	 * -l <text>		the text label for this run; used to build output filenames
	 * -t <int>			timer period for reporting progress of file input to screen
	 * -s				calculate and record the support of the dQ matrix
	 * -v --v ---v		differing levels of screen output verbosity
	 * -c <int>			record the aglomerated network at step <int> (typically, this is the step location at which the modularity is known to be maximum)
	 * 
	 * In order to set arguments, please use argument with '-' and optional value, if required.
	 * 
	 * arguments.put("-f", "/path/to/file");
	 * arguments.put("-v", "");
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
		this.logger.log("Start running fast modularity clustering via command " + this.commandString);
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
		
		this.logger.log("Finished clustering via fast modularity clustering");

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
