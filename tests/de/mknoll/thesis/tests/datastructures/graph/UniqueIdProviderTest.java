package de.mknoll.thesis.tests.datastructures.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.IdentifierRecommenderObjectMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.UniqueIdProvider;



/**
 * Class implements testcase for unique ID provider
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class UniqueIdProviderTest {

	@Test
	public void testUniqueIdProvider() {
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		UniqueIdProvider uniqueIdProvider = new UniqueIdProvider(map);
		RecommenderObject recObject = new RecommenderObject("1234");
		String uniqueId = uniqueIdProvider.getId(recObject);
		assertTrue(uniqueId.equals("1"));
		assertTrue(map.containsExternalId("1"));
		assertTrue(map.containsInternalId("1234"));
		assertTrue(map.getRecommenderObjectByExternalId("1") == recObject);
		assertTrue(map.getRecommenderObjectByInternalId("1234") == recObject);
		
		assertTrue(uniqueIdProvider.getId(recObject).equals("1"));
		
		RecommenderObject recObject2 = new RecommenderObject();
		assertTrue(uniqueIdProvider.getId(recObject2).equals("2"));
	}
	
	
	
	@Test
	public void getIdReturnsCorrectIdForRecommenderObject() {
		RecommenderObject recObject1 = new RecommenderObject("3");
		RecommenderObject recObject2 = new RecommenderObject("4");
		RecommenderObject recObject3 = new RecommenderObject("5");
		RecommenderObject recObject4 = new RecommenderObject("6");
		
		IdentifierRecommenderObjectMap map = new IdentifierRecommenderObjectMap();
		UniqueIdProvider uniqueIdProvider = new UniqueIdProvider(map);
		
		// Testing created ID in arbitrary order
		assertTrue(uniqueIdProvider.getId(recObject1).equals("1"));
		assertTrue(uniqueIdProvider.getId(recObject1).equals("1"));
		assertTrue(uniqueIdProvider.getId(recObject2).equals("2"));
		assertTrue(uniqueIdProvider.getId(recObject1).equals("1"));
		assertTrue(uniqueIdProvider.getId(recObject3).equals("3"));
		assertTrue(uniqueIdProvider.getId(recObject2).equals("2"));
		assertTrue(uniqueIdProvider.getId(recObject4).equals("4"));
		assertTrue(uniqueIdProvider.getId(recObject3).equals("3"));
		assertTrue(uniqueIdProvider.getId(recObject2).equals("2"));
		assertTrue(uniqueIdProvider.getId(recObject1).equals("1"));
	}

}
