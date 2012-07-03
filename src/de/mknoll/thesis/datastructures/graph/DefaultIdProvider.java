package de.mknoll.thesis.datastructures.graph;



/**
 * Class implements method that returns default ID (docId) from 
 * a given recommender object. 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class DefaultIdProvider implements IdProvider {

	/**
	 * Returns default ID from given recommender object
	 * 
	 * @return Default Id for given recommender object
	 * @Override
	 */
	public String getId(RecommenderObject recommenderObject) {
		return recommenderObject.getDocId(); 
	}

}
