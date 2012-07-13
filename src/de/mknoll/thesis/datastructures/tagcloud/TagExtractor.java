package de.mknoll.thesis.datastructures.tagcloud;

import java.util.ArrayList;
import java.util.List;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements a tag extractor
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.TagExtractorTest
 */
public class TagExtractor {
	
	/**
	 * Holds delimiter for splitting words in string
	 */
	private String delimiter = "\\s+";
	
	
	
	/**
	 * Holds regex for replacing special chars in tagnames
	 */
	private String specialCharReplacement = "[^\\p{L}\\p{N}]";

	
	
	/**
	 * Returns a list of tags for a given string
	 * 
	 * @param string String to extract tags from
	 * @return Extracted tags of given string
	 */
	public List<Tag> extractTags(String string) {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String tagName : string.split(this.delimiter)) {
			tags.add(new Tag(this.replaceSpecialChars(tagName)));
		}
		return tags;
	}
	
	
	
	/**
	 * Replaces all special chars in given tagname
	 * 
	 * @see http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact
	 * @param tagName
	 * @return
	 */
	private String replaceSpecialChars(String tagName) {
		return tagName.replaceAll(this.specialCharReplacement, "");
	}

}
