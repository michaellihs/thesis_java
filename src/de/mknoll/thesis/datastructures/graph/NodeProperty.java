package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;



/**
 * A node property is an arbitrary object that can be attached
 * to a node as property.
 * 
 * @author Michael Knoll <mimi@kaktsuteam.de>
 */
public interface NodeProperty {

	/**
	 * Returns a map of properties, no matter what information is
	 * stored in the property to be stored in node storage.
	 * 
	 * @return Map of properties for this property.
	 */
	public HashMap<String,Object> getProperties();
	
}