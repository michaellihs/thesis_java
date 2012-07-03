package de.mknoll.thesis.externaltools.wrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a wrapper for calling R scripts on command line
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class R {
	
	private static final String SCRIPTS_PATH = "/Users/mimi/Dropbox/Diplomarbeit/Workspace/thesis/scripts/r/";
	
	
	
	/**
	 * Holds base path of R scripts
	 */
	private String scriptsBasePath;
	
	
	
	/**
	 * @Inject
	 */
	private LoggerInterface logger;
	
	
	
	public R(String scriptsBasePath) {
		this.scriptsBasePath = scriptsBasePath;
	}
	
	
	
	public R() {
		this.scriptsBasePath = R.SCRIPTS_PATH;
	}
	
	
	
	public boolean run(String script, String arguments) {
		Runtime run = Runtime.getRuntime() ;
		String command = this.buildCommandString(script, arguments);
		boolean success = false;
		try {
			Process process = run.exec(command) ;
			BufferedReader outputBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String output;
			while ((output = outputBuffer.readLine()) != null) {
				this.logger.log(output);
			}
			outputBuffer.close();
			
			BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((output = errorBuffer.readLine()) != null) {
				this.logger.log(output);
			}
			
			process.getErrorStream().close();
			process.getOutputStream().close();
			process.getInputStream().close();
			
			success = (process.waitFor() == 0);
		} catch (Exception e) {
			this.logger.log("Error when trying to run R command " + script + " with arguments " + arguments + " and resulting command " + command);
		}
		return success;
	}



	private String buildCommandString(String script, String arguments) {
		return this.scriptsBasePath + script + " " + arguments; 
	}
	
}
