package de.mknoll.thesis.datastructures.graph;



/**
 * Interface for ID providers.
 * 
 * An ID is a unique identifier for a recommendation object.
 * Whenever exporting a recommendation structure, we need a unique identifier
 * for the recommended objects within this structure.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface IdProvider {

	/**
	 * Returns ID for given recommender object
	 * 
	 * @param Recommender object to get ID from
	 * @return ID for given recommender object
	 */
	public String getId(RecommenderObject recommenderObject);
	
}
