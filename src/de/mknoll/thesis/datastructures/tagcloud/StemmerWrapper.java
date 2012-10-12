package de.mknoll.thesis.datastructures.tagcloud;

import java.util.ArrayList;
import java.util.List;

import org.mcavallo.opencloud.Tag;





/**
 * Class implements a wrapper for a stemmer.
 * 
 * This wrapper has some helper methods for easy usage of stemmers.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class StemmerWrapper {
	
	/**
	 * Holds tag stem map used to map stem to tag
	 */
	private TagStemMap tagStemMap;

	
	
	/**
	 * Constructor takes stemmer to be used (e.g. german / english stemmer...)
	 * 
	 * @param stemmer Stemmer to be used within this wrapper
	 */
	public StemmerWrapper(TagStemMap tagStemMap) {
		this.tagStemMap = tagStemMap;
	}
	
	
	
	/**
	 * Stems a list of given tags and returns list of stemmed tags
	 * 
	 * @param tags List of tags to be stemmed
	 * @return List of stemmed tags
	 */
	public List<Tag> stemTags(List<Tag> tags) {
		List<Tag> stemmedTags = new ArrayList<Tag>();
		
		for (Tag tag : tags) {
			Tag stemmedTag = new Tag(this.tagStemMap.put(tag));
			stemmedTags.add(stemmedTag);
		}
		
		return stemmedTags;
	}
	
}
