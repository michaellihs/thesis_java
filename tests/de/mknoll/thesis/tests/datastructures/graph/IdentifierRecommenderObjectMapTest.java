package de.mknoll.thesis.tests.datastructures.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.IdentifierRecommenderObjectMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements testcase for identifier --> recommender object mapping
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.IdentifierRecommenderObjectMap
 */
public class IdentifierRecommenderObjectMapTest {

	@Test
	public void testInsertRecommenderObjectByInternalExternalId() {
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		RecommenderObject recommenderObject = new RecommenderObject();
		map.insertRecommenderObjectByInternalExternalId(recommenderObject, "1", "2");
		assertTrue(map.getRecommenderObjectByExternalId("2") == recommenderObject);
		assertTrue(map.getRecommenderObjectByInternalId("1") == recommenderObject);
	}
	
	

	@Test
	public void testGetExternalIdByRecommenderObject() {
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		RecommenderObject recommenderObject = new RecommenderObject();
		map.insertRecommenderObjectByInternalExternalId(recommenderObject, "1", "2");
		assertTrue(map.getExternalIdByRecommenderObject(recommenderObject) == "2");
	}
	
	

	@Test
	public void testContainsInternalId() {
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		RecommenderObject recommenderObject = new RecommenderObject();
		map.insertRecommenderObjectByInternalExternalId(recommenderObject, "1", "2");
		assertTrue(map.containsInternalId("1"));
	}
	
	

	@Test
	public void testContainsExternalId() {
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		RecommenderObject recommenderObject = new RecommenderObject();
		map.insertRecommenderObjectByInternalExternalId(recommenderObject, "1", "2");
		assertTrue(map.containsExternalId("2"));
	}

}
