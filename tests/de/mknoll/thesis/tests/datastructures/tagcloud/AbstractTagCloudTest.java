package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.mcavallo.opencloud.Cloud;



/**
 * Class implements abstract base class for tag cloud test classes
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public abstract class AbstractTagCloudTest {

	public AbstractTagCloudTest() {
		super();
	}
	
	

	protected void assertContainsAllTags(Cloud cloud, String ... tags) throws Exception {
		for (String tagName: tags) {
			if (!cloud.containsName(tagName)) {
				throw new Exception("Cloud was expected to contain " + tagName + " but did not!");
			}
			Assert.assertTrue(cloud.containsName(tagName));
		}
	}

}