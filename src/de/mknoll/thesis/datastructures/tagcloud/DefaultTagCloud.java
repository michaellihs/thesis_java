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
 * Class implements default tag cloud
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
	
	
	
	public DefaultTagCloud() {
		this.buildAndSetDefaultFilters();
	}



	private void buildAndSetDefaultFilters() {
		Filter<Tag> minLengthFilter = new MinLengthFilter(2);
		Filter<Tag> dictionaryFilter = new DictionaryFilter(ResourceBundle.getBundle(DefaultTagCloud.blacklist, Locale.GERMAN));
		Filter<Tag> defaultFilter = new AndFilter<Tag>(minLengthFilter, dictionaryFilter);
		this.addInputFilter(defaultFilter);
	}

}
