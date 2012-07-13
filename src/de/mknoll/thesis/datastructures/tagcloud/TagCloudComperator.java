package de.mknoll.thesis.datastructures.tagcloud;

import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;



/**
 * Class implements a comperator framework for tag clouds.
 * Given multiple strategies for comparing tag clouds,
 * measures of similarity are calculated.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class TagCloudComperator {
	
	/**
	 * Holds comparator strategy
	 */
	private TagComparatorStrategy comperatorStrategy;
	
	
	
	/**
	 * Constructor takes comparator strategy
	 * 
	 * @param comparatorStrategy Comparator strategy used for comparing tag clouds
	 */
	public TagCloudComperator(TagComparatorStrategy comparatorStrategy) {
		this.comperatorStrategy = comparatorStrategy;
	}

	
	
	/**
	 * Compares given tagclouds and calculates similarity measure
	 * 
	 * @param cloud1 First cloud to be compared
	 * @param cloud2 Second cloud to be compared
	 * @return Similarity measure for given clouds
	 */
	public Float compare(Cloud cloud1, Cloud cloud2) {
		return this.compareTags(cloud1.allTags(), cloud2.allTags());
	}
	
	
	
	protected Float compareTags(List<Tag> tags1, List<Tag> tags2) {
		return this.comperatorStrategy.compare(tags1, tags2);
	}
	
}
