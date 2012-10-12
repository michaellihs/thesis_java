package de.mknoll.thesis.tests.datastructures.graph;

import org.junit.Assert;
import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements testcase for a recommender object
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.RecommenderObject
 */
public class RecommenderObjectTest {

	@Test
	public void constructorReturnsExpectedObjectForGivenId() {
		RecommenderObject recObj = new RecommenderObject("docId:1234");
		Assert.assertTrue(recObj.getDocId().equals("docId:1234"));
	}
	
	
	
	@Test
	public void constructorReturnsExpectedObjectForGivenIdAndDescription() {
		RecommenderObject recObj = new RecommenderObject("docId:1234", "description");
		Assert.assertTrue(recObj.getDocId().equals("docId:1234"));
		Assert.assertTrue(recObj.getDescription().equals("description"));
	}
	
	
	
	@Test
	public void constructorReturnsExpectedObjectForGivenIdAndDescriptionAndIsbn() {
		RecommenderObject recObj = new RecommenderObject("docId:1234", "description", "123456789012");
		Assert.assertTrue(recObj.getDocId().equals("docId:1234"));
		Assert.assertTrue(recObj.getDescription().equals("description"));
		Assert.assertTrue(recObj.getIsbn12().equals("123456789012"));
	}
	
	
	
	@Test
	public void getTagCloudReturnsExpectedTagCloud() {
		RecommenderObject recObj = new RecommenderObject("docId:1234", "tag1 tag2 tag3");
		Assert.assertTrue(recObj.getTagCloud().containsName("tag1"));
		Assert.assertTrue(recObj.getTagCloud().containsName("tag2"));
		Assert.assertTrue(recObj.getTagCloud().containsName("tag3"));
	}
	
}
