package de.mknoll.thesis.tests.datastructures.tagcloud;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;



/**
 * Testcase implements some tests for OpenCloud Cloud
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class CloudTest {

	@Test
	public void getDifferenceReturnsExpectedTags() {
		Cloud firstCloud = new Cloud();
		Cloud secondCloud = new Cloud();
		
		Tag tag1 = new Tag("test1");
		Tag tag2 = new Tag("test2");
		Tag tag3 = new Tag("test3");
		Tag tag4 = new Tag("test4");
		Tag tag5 = new Tag("test5");
		
		
		firstCloud.addTag(tag1);
		firstCloud.addTag(tag2);
		firstCloud.addTag(tag3);
		firstCloud.addTag(tag4);
		
		secondCloud.addTag(tag3);
		secondCloud.addTag(tag4);
		secondCloud.addTag(tag5);
		
		List<Tag> difference = firstCloud.getDifference(secondCloud);
		
		Assert.assertTrue(difference.contains(tag1) && difference.contains(tag2) && difference.contains(tag5));
		Assert.assertFalse(difference.contains(tag3) || difference.contains(tag4));
	}
	
}
