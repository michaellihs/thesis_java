package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Locale;
import java.util.ResourceBundle;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.filters.AndFilter;
import org.mcavallo.opencloud.filters.DictionaryFilter;
import org.mcavallo.opencloud.filters.Filter;
import org.mcavallo.opencloud.filters.MinLengthFilter;



/**
 * Class implements default tag cloud as used throughout thesis.
 * 
 * Default tag cloud has following filters set as default:
 * - MinLengthFilter(2) -- only accepts tags if their name is longer than 2
 * - DictionaryFilter -- only accepts tags if their names are not in short blacklist
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.DefaultTagCloudTest
 */
public class DefaultTagCloud extends Cloud {

	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private static final String blacklist = "dictionary_blacklist";
	
	
	
	/**
	 * Copy constructor
	 * 
	 * @param other
	 */
	public DefaultTagCloud(Cloud other) {
		super();
		
		this.setMinWeight(other.getMinWeight());
        this.setMaxWeight(other.getMaxWeight());
        this.setMaxTagsToDisplay(other.getMaxTagsToDisplay());
        this.setThreshold(other.getThreshold());
        this.setNormThreshold(other.getNormThreshold());
        this.setWordPattern(other.getWordPattern());
        this.setTagLifetime(other.getTagLifetime());
        this.setTagCase(other.getTagCase());
        this.setLocale(other.getLocale());
        this.setDefaultLink(other.getDefaultLink());
        this.setRounding(other.getRounding());
	        
		this.buildAndSetDefaultFilters();
		
		// Add tags only after filter queue is initialized
		this.addTags(other.allTags());
		
	}
	
	
	
	public DefaultTagCloud() {
		this.buildAndSetDefaultFilters();
	}
	
	
	
	public DefaultTagCloud(String ... tags) {
		super();
		this.buildAndSetDefaultFilters();
		this.addTags(tags);
	}



	private void buildAndSetDefaultFilters() {
		Filter<Tag> minLengthFilter = new MinLengthFilter(2);
		Filter<Tag> dictionaryFilter = new DictionaryFilter(ResourceBundle.getBundle(DefaultTagCloud.blacklist, Locale.GERMAN));
		Filter<Tag> defaultFilter = new AndFilter<Tag>(minLengthFilter, dictionaryFilter);
		this.addInputFilter(defaultFilter);
	}
	
	
	
	@Override
	public String toString() {
		String result = new String();
		for (Tag tag : this.allTags(new Tag.NameComparatorAsc())) {
			result += "\"" + tag.getName() + "\", ";
		}
		return result;
	}
	
	

	public void addTags(String ... tags) {
		for(String tag : tags) {
			this.addTag(tag);
		}
	}

}
