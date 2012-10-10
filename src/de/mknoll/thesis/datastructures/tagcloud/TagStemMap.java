package de.mknoll.thesis.datastructures.tagcloud;

import java.util.HashMap;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements a mapping between a tag and its stem.
 * 
 * TODO Class has to be created for each language individually
 * 
 * @author Michael Knoll <mimi@kaktusteam.de
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.TagStemMapTest
 */
public class TagStemMap {

	private static final long serialVersionUID = 1L;

	
	
	/**
	 * Holds a mapping from stem to tags
	 * 
	 * Each stem can have 1..n tags associated with it.
	 */
	private HashMap<String, HashMap<Tag, Integer>> stemToTagMap;
	
	
	
	/**
	 * Holds a mapping from tag to stem
	 * 
	 * Each tag can only have a single stem associated with it.
	 */
	private HashMap<Tag, String> tagToStemMap;

	
	
	/**
	 * Holds a stemmer used for this mapping
	 */
	private Stemmer stemmer;
	
	
	
	/**
	 * Constructor for this class
	 */
	public TagStemMap(Stemmer stemmer) {
		this.stemmer = stemmer;
		this.tagToStemMap = new HashMap<Tag, String>();
		this.stemToTagMap = new HashMap<String, HashMap<Tag,Integer>>();
	}
	
	
	
	/**
	 * Puts given tag into mapping
	 * 
	 * @param tag Tag to be added to mapping
	 * @return Stem for given tag
	 */
	public String put(Tag tag) {
		if (this.tagToStemMap.containsKey(tag.getName())) {
			return this.tagToStemMap.get(tag.getName());
		} else {
			return this.putNewTagToMapping(tag);
		}
	}
	
	
	
	/**
	 * Returns stem for given tag
	 * 
	 * @param tag Tag to return stem for
	 * @return Stem for given tag
	 */
	public String getStemForTag(Tag tag) {
		return this.tagToStemMap.get(tag);
	}
	
	
	
	/**
	 * Returns true, if given tag is already included in mapping
	 * 
	 * @param tag Tag to check whether it is included in mapping
	 * @return True, if given tag is included in mapping
	 */
	public Boolean containsTag(Tag tag) {
		return this.tagToStemMap.containsKey(tag);
	}
	
	
	
	/**
	 * Returns true, if given stem is already included in mapping
	 * 
	 * @param stem Stem to check whether it is included in mapping
	 * @return True, if given stem is included in mapping
	 */
	public Boolean containsStem(String stem) {
		return this.stemToTagMap.containsKey(stem);
	}
	
	
	
	/**
	 * Returns tag that is most often registered for given stem or null if no tag had been registered
	 * 
	 * @param stem Stem to get most important tag for
	 * @return Most important tag for given stem
	 */
	public Tag getMostImportantTagForStem(String stem) {
		if (!this.containsStem(stem)) {
			return null;
		}

		// TODO cache this!
		Tag mostImportantTag = null;
		Integer maxCount = 0;
		
		HashMap<Tag, Integer> entry = this.stemToTagMap.get(stem);
		
		for (Tag tag : entry.keySet()) {
			if (entry.get(tag) > maxCount) {
				mostImportantTag = tag;
			}
		}
		
		return mostImportantTag;
	}
	
	
	
	/**
	 * Returns name of most important tag for given 
	 * 
	 * @param stem Stem to get most important tag name for
	 * @return Name of most important tag for given stem
	 */
	public String getMostImportantTagNameForStem(String stem) {
		Tag tag = this.getMostImportantTagForStem(stem); 
		if (tag != null) return tag.getName(); 
		else return null;
	}



	private String putNewTagToMapping(Tag tag) {
		String stem = this.stemmer.stem(tag.getName());
		this.tagToStemMap.put(tag, stem);
		
		this.addStemToTagMap(stem, tag);
		
		return stem;
	}



	private void addStemToTagMap(String stem, Tag tag) {
		if (!this.stemToTagMap.containsKey(stem)) {
			// We have no mapping for this stem yet --> we create one
			this.stemToTagMap.put(stem,	new HashMap<Tag, Integer>());
		}
		
		if (this.stemToTagMap.get(stem).containsKey(tag)) {
			// Tag is already registered as long-form for stem --> increase count by one
			this.stemToTagMap.get(stem).put(tag, this.stemToTagMap.get(stem).get(tag) + 1);
		} else {
			// Tag is not yet registered, we register tag with count 1
			this.stemToTagMap.get(stem).put(tag, 1);
		}
		
	}

}
