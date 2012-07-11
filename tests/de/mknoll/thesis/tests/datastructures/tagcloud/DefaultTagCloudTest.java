package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;



/**
 * Class implements a test for a default tag cloud
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud
 */
public class DefaultTagCloudTest {

	@Test
	public void expectedTagsCanBeAdded() {
		DefaultTagCloud cloud = new DefaultTagCloud();
		Tag tag0 = new Tag("1"); // should be filtered due to min length
		Tag tag1 = new Tag("ein"); // should be filtered due to stopword
		Tag tag2 = new Tag("Analysis"); // should not be filtered
		cloud.addTag(tag0);
		cloud.addTag(tag1);
		cloud.addTag(tag2);
		
		Assert.assertTrue(cloud.tags().contains(tag2));
		Assert.assertFalse(cloud.tags().contains(tag0));
		Assert.assertFalse(cloud.tags().contains(tag1));
	}
	
}