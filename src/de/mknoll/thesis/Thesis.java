package de.mknoll.thesis;

import de.mknoll.thesis.framework.core.Bootstrap;



/**
 * Class implements main class for Diploma Thesis framework.
 * 
 * Framework is implemented to conduct several tests with graph clustering 
 * algorithms working on recommender data of Bibtip library recommender system.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Thesis {
	
	/**
	 * If set to true, logging will be enabled
	 */
	public final static boolean ENABLE_LOGGING = true;
	
	

	/**
	 * Runs framework
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO use cli argument for configuration file
		Bootstrap bootstrap = new Bootstrap("/Users/mimi/Dropbox/Diplomarbeit/Workspace/thesis/configuration/configuration.yaml");
		bootstrap.run();
	}

}
