package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Locale;
import java.util.ResourceBundle;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.filters.DictionaryFilter;
import org.mcavallo.opencloud.filters.Filter;



/**
 * Class implements tag cloud that is filtered by extended 
 * stopwordlist taken from 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see @see de.mknoll.thesis.tests.datastructures.tagcloud.StopWordFilteredTagCloudTest
 */
public class StopWordFilteredTagCloud extends Cloud {

	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private static final String blacklist = "extended_blacklist";
	
	
	
	/**
	 * Copy constructor
	 * 
	 * All tags and settings are copied from given cloud respecting local filtering
	 * 
	 * @param other Tag cloud to be copied into this one
	 */
	public StopWordFilteredTagCloud(Cloud other) {
		super();
		
		this.initialize();
		
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
		
		this.addTags(other.allTags());
	}
	
	
	
	public StopWordFilteredTagCloud() {
		this.buildAndSetDefaultFilters();
	}



	private void buildAndSetDefaultFilters() {
		Filter<Tag> dictionaryFilter = new DictionaryFilter(ResourceBundle.getBundle(StopWordFilteredTagCloud.blacklist, Locale.GERMAN));
		this.addInputFilter(dictionaryFilter);
	}

	
	
	/**
	 * We need this method here, as we do NOT overwrite DefaultTagCloud (who already has this method implemented)
	 * but Cloud, which does not have this method implemented!
	 * 
	 * @Override
	 */
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
	
	
	
	/**
	 * Template method for initializing this class
	 */
	protected void initialize() {
		// Nothing to do here but use it to initialize extended classes
	}
	
}
