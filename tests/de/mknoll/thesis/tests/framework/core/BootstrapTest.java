package de.mknoll.thesis.tests.framework.core;

import org.junit.Test;

import de.mknoll.thesis.framework.core.Bootstrap;



/**
 * Class implements test for bootstrap.
 * 
 * Bootstrap is not easy to be tested, so testcase here is 
 * rather small.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class BootstrapTest {
	
	@Test
	public void constructorReturnsBootstrapInstance() {
		Bootstrap bootstrap = new Bootstrap("/Users/mimi/Dropbox/Diplomarbeit/Workspace/thesis/configuration/configuration.yaml");
	}
	
}
