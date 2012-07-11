package de.mknoll.thesis.datastructures.tagcloud;

import org.mcavallo.opencloud.Cloud;



/**
 * Interface for classes that can export a tag cloud
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface TagCloudContainer {
	
	public Cloud getTagCloud();

}
