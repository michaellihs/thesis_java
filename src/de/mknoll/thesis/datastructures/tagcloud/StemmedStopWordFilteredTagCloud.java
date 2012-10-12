package de.mknoll.thesis.datastructures.tagcloud;

import org.apache.lucene.analysis.de.GermanStemmer;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;



/**
 * Class implements a tag cloud that is stop word filtered
 * and tags are stemmed.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class StemmedStopWordFilteredTagCloud extends StopWordFilteredTagCloud {

	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Holds mapping between tag and stemmed form of tag
	 * 
	 * TODO if we need the stemmer to work with other languages, we have to somehow make this injectable! 
	 */
	private TagStemMap tagStemMap = new TagStemMap(new GermanStemmer());
	
	
	
	/**
	 * Copy constructor
	 * 
	 * @param other Cloud to be copied into this cloud
	 */
	public StemmedStopWordFilteredTagCloud(Cloud other) {
		super(other);
	}

	
	
	/**
	 * Method stems given tag than adds it to this cloud.
	 */
	public void addTag(Tag tag) {
		Tag stemmedTag = new Tag(this.tagStemMap.put(tag));
		super.addTag(stemmedTag);
	}
	
}
