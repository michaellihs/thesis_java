package de.mknoll.thesis.tests.datastructures.tagcloud;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.TagExtractor;



/**
 * Class implements test for a tag extractor
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.TagExtractor
 */
public class TagExtractorTest {

	@Test
	public void extractTagsReturnsExpectedTags() {
		TagExtractor tagExtractor = new TagExtractor();
		String testString = "tag1 tag2 tag3";
		List<Tag> tags = tagExtractor.extractTags(testString);
		Assert.assertTrue(tags.size() == 3);
		Assert.assertTrue(tags.get(0).getName().equals("tag1"));
		Assert.assertTrue(tags.get(1).getName().equals("tag2"));
		Assert.assertTrue(tags.get(2).getName().equals("tag3"));
	}
	
	
	
	@Test
	public void extractTagsReplacesSpecialCharsFromTags() {
		TagExtractor tagExtractor = new TagExtractor();
		String testString = "$tag?with-special::chars\"";
		List<Tag> tags = tagExtractor.extractTags(testString);
		Assert.assertTrue(tags.size() == 1);
		Assert.assertTrue(tags.get(0).getName().equals("tagwithspecialchars"));
		
		testString = "\"quotedTag\" -anotherspecialchartag?";
		tags = tagExtractor.extractTags(testString);
		Assert.assertTrue(tags.size() == 2);
		Assert.assertTrue(tags.get(0).getName().equals("quotedTag"));
		Assert.assertTrue(tags.get(1).getName().equals("anotherspecialchartag"));
	}
	
}