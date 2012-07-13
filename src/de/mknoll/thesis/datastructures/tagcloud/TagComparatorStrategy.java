package de.mknoll.thesis.datastructures.tagcloud;

import java.util.List;

import org.mcavallo.opencloud.Tag;



/**
 * Interface for classes that implement tag comperators
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface TagComparatorStrategy {

	public Float compare(List<Tag> tags1, List<Tag> tags2);
	
}
