package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;



/**
 * Class implements method that returns a unique numerical UID for a 
 * given recommender object
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class UniqueIdProvider implements IdProvider {
	
	/**
	 * Holds identifier mapping
	 */
	private IdentifierRecommenderObjectMap mapping;
	
	
	
	/**
	 * Holds last internal UID used for mapping.
	 */
	private Integer lastId = 1;
	
	
	
	/**
	 * Constructor takes mapping table (most likely from DI)
	 * 
	 * @param Mapping table
	 */
	public UniqueIdProvider(IdentifierRecommenderObjectMap mapping) {
		this.mapping = mapping;
	}

	
	
	/**
	 * Returns default ID from given recommender object
	 * 
	 * @return Default Id for given recommender object
	 * @Override
	 */
	public String getId(RecommenderObject recommenderObject) {
		if (!this.mapping.containsInternalId(recommenderObject.getDocId())) {
			this.mapping.insertRecommenderObjectByInternalExternalId(
					recommenderObject, 
					recommenderObject.getDocId(), 
					(this.lastId++).toString()
			);
			recommenderObject.setInternalId(this.lastId);
		}
		String externalId = this.mapping.getExternalIdByRecommenderObject(recommenderObject); 
		return externalId; 
	}

}
