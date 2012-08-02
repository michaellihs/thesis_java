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
 * Class implements tag cloud that is filtered by extended 
 * stopwordlist taken from 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class StopWordFilteredTagCloud extends Cloud {

	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private static final String blacklist = "extended_blacklist";
	
	
	
	public StopWordFilteredTagCloud(Cloud other) {
		// this obviously does not work, as we add tags BEFORE registering filters
		// super(other);
		this.buildAndSetDefaultFilters();
		
		// so we have to do this
		for (Tag tag : other.tags()) {
			this.addTag(tag);
		}
	}
	
	
	
	public StopWordFilteredTagCloud() {
		this.buildAndSetDefaultFilters();
	}



	private void buildAndSetDefaultFilters() {
		Filter<Tag> dictionaryFilter = new DictionaryFilter(ResourceBundle.getBundle(StopWordFilteredTagCloud.blacklist, Locale.GERMAN));
		this.addInputFilter(dictionaryFilter);
	}
	
	
	
	@Override
	public String toString() {
		String result = new String();
		for (Tag tag : this.tags()) {
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
