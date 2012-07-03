package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;



/**
 * Holds a map of recommender objects to
 * --> internal IDs (doc id)
 * --> external IDs (used for some clustering etc.)
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.IdentifierRecommenderObjectMapTest
 */
public class IdentifierRecommenderObjectMap {

	private HashMap<String, RecommenderObject> externalIdToRecommenderObjectMap;
	
	
	
	private HashMap<String, RecommenderObject> internalIdToRecommenderObjectMap;
	
	
	
	private HashMap<String, String> recommenderObjectIdentifierToExternalIdentifierMap;
	
	
	
	public IdentifierRecommenderObjectMap() {
		this.externalIdToRecommenderObjectMap = new HashMap<String, RecommenderObject>();
		this.internalIdToRecommenderObjectMap = new HashMap<String, RecommenderObject>();
		this.recommenderObjectIdentifierToExternalIdentifierMap = new HashMap<String, String>();
	}
	
	
	
	public void insertRecommenderObjectByInternalExternalId(RecommenderObject recommenderObject, String internalId, String externalId) {
		this.internalIdToRecommenderObjectMap.put(internalId, recommenderObject);
		this.externalIdToRecommenderObjectMap.put(externalId, recommenderObject);
		this.recommenderObjectIdentifierToExternalIdentifierMap.put(recommenderObject.getDocId(), externalId);
	}
	
	
	
	public RecommenderObject getRecommenderObjectByExternalId(String externalId) {
		return this.externalIdToRecommenderObjectMap.get(externalId);
	}
	
	
	
	public RecommenderObject getRecommenderObjectByInternalId(String internalId) {
		return this.internalIdToRecommenderObjectMap.get(internalId);
	}
	
	
	
	public String getExternalIdByRecommenderObject(RecommenderObject recommenderObject) {
		return this.recommenderObjectIdentifierToExternalIdentifierMap.get(recommenderObject.getDocId());
	}



	public boolean containsInternalId(String internalId) {
		return this.internalIdToRecommenderObjectMap.containsKey(internalId);
	}
	
	
	
	public boolean containsExternalId(String externalId) {
		return this.externalIdToRecommenderObjectMap.containsKey(externalId);
	}
	
}
