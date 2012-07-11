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
	
	private String delimiter = "\\s+";

	public List<Tag> extractTags(String string) {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String tagName : string.split(this.delimiter)) {
			tags.add(new Tag(tagName));
		}
		return tags;
	}

}
