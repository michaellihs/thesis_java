package de.mknoll.thesis.framework.logger;

import de.mknoll.thesis.Thesis;



/**
 * Class implements a logger that writes log messages to console.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class ConsoleLogger implements LoggerInterface {

	@Override
	public void log(String message) {
		if (Thesis.ENABLE_LOGGING) {
			System.out.println(this.enrichLogMessage(message));
		}
	}

	
	
	private String enrichLogMessage(String message) {
		String enrichedMessage = "";
		
		StackTraceElement e = Thread.currentThread().getStackTrace()[3];
		enrichedMessage = 
			e.getFileName() + "::" + 
			e.getClassName() + "::" +  
			e.getMethodName() + "::" + 
			e.getLineNumber() + "\n--- " + message;
		
		return enrichedMessage;
	}
}
