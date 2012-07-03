package de.mknoll.thesis.tests.datastructures.graph;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.tests.div.MockGraphBuilder;
import junit.framework.Assert;
import junit.framework.TestCase;



/**
 * Class implements testcase for recommendation graph class.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.RecommendationGraph
 */
public class RecommendationGraphTest {

	@Test
	public void newRecommendationGraphCanBeCreated() {
		IdNodeMap idNodeMap = new DefaultIdNodeMap();
		RecommendationGraph g = new RecommendationGraph(idNodeMap);
	}
	
	
	
	@Test 
	public void recommendationGraphThrowsExceptionIfTryingToAddRecommenderObjectWithoutDocId() {
		try {
			IdNodeMap idNodeMap = new DefaultIdNodeMap();
			RecommendationGraph g = new RecommendationGraph(idNodeMap);
			RecommenderObject rec = new RecommenderObject();
			g.addRecommenderObject(rec);
			fail("RecommendationGraph did not throw exception although trying to add recommender object that did not have doc id set!");
		} catch (Exception e) {
			// Nothing to do - test passed
		}
	}
	
	
	
	@Test
	public void recommendationsCanBeAddedToRecommendationGraphByRecommendationObjects() throws Exception {
		IdNodeMap idNodeMap = new DefaultIdNodeMap();
		
		// Creating some recommender objects
		RecommenderObject recObject1 = new RecommenderObject("ol:1");
		RecommenderObject recObject2 = new RecommenderObject("ol:2");
		RecommenderObject recObject3 = new RecommenderObject("ol:3");
		RecommenderObject recObject4 = new RecommenderObject("ol:4");
		
		// Creating new empty recommendation graph
		RecommendationGraph g = new RecommendationGraph(idNodeMap);
		
		// Adding some vertices
		g.addRecommenderObject(recObject1);
		g.addRecommenderObject(recObject2);
		g.addRecommenderObject(recObject3);
		g.addRecommenderObject(recObject4);
		
		// Adding some edges
		Recommendation r1 = g.addRecommendation(recObject1, recObject2);
		Recommendation r2 = g.addRecommendation(recObject1, recObject3);
		Recommendation r3 = g.addRecommendation(recObject1, recObject4);
		g.setEdgeWeight(r1, 0.5);
		g.setEdgeWeight(r2, 0.75);
		g.setEdgeWeight(r3, 0.8);
		
		assertTrue(r1.getWeight() == 0.5);
	}
	
	
	
	@Test
	public void recommendationsCanBeAddedToRecommendationGraphByRecommendation() throws Exception {
		IdNodeMap idNodeMap = new DefaultIdNodeMap();
		
		// Creating some recommender objects
		RecommenderObject recObject1 = new RecommenderObject("ol:1");
		RecommenderObject recObject2 = new RecommenderObject("ol:2");
		
		// Creating new empty recommendation graph
		RecommendationGraph g = new RecommendationGraph(idNodeMap);
		Recommendation rec = g.addRecommendation(recObject1, recObject2, 0.5);
		
		// Asserting, that added edge fulfills previous parameters
		assertTrue(rec.getSourceRecommendation() == recObject1);
		assertTrue(rec.getTargetRecommendation() == recObject2);
		assertTrue(rec.getWeight() == 0.5);
	}
	
	
	
	@Test
	public void recommendationGraphIsBuildUpCorrectlyUsingMultipleRecommenderObjectsForTheSameDocId() throws Exception {
		IdNodeMap idNodeMap = new DefaultIdNodeMap();
		
		// Creating some recommender objects
		RecommenderObject recObject1 = new RecommenderObject("ol:1");
		RecommenderObject recObject2 = new RecommenderObject("ol:2");
		
		// Rec graph should recognize two rec objects being added with same ID "ol:1"
		RecommenderObject recObject3 = new RecommenderObject("ol:1");
		RecommenderObject recObject4 = new RecommenderObject("ol:3");
		
		// Creating new empty recommendation graph
		RecommendationGraph g = new RecommendationGraph(idNodeMap);
		Recommendation rec1 = g.addRecommendation(recObject1, recObject2, 0.5);
		Recommendation rec2 = g.addRecommendation(recObject3, recObject4, 0.5);
		
		// Assert correct size of graph
		Assert.assertEquals(g.edgeSet().size(), 2);
		Assert.assertEquals(g.vertexSet().size(), 3);
	}
	
	
	
	@Test
	public void getNodeByInternalIdReturnsExpectedNodes() {
		RecommendationGraph graphMock = MockGraphBuilder.createRecommendationTestGraph();
		
		Node node1 = graphMock.getNodeByInternalId(1);
		Assert.assertEquals(1, node1.internalId());
		Node node2 = graphMock.getNodeByInternalId(2);
		Assert.assertEquals(2, node2.internalId());
		Node node3 = graphMock.getNodeByInternalId(3);
		Assert.assertEquals(3, node3.internalId());
		Node node4 = graphMock.getNodeByInternalId(4);
		Assert.assertEquals(4, node4.internalId());
		Node node5 = graphMock.getNodeByInternalId(5);
		Assert.assertEquals(5, node5.internalId());
		Node node6 = graphMock.getNodeByInternalId(6);
		Assert.assertEquals(6, node6.internalId());

	}
	
	
	
	@Test
	public void getNeighborsOfReturnsExpectedNodes() {
		RecommendationGraph graphMock = MockGraphBuilder.createRecommendationTestGraph();
		Node node1 = graphMock.getNodeByInternalId(1);
		Node node2 = graphMock.getNodeByInternalId(2);
		Node node3 = graphMock.getNodeByInternalId(3);
		Node node4 = graphMock.getNodeByInternalId(4);
		Node node5 = graphMock.getNodeByInternalId(5);
		Node node6 = graphMock.getNodeByInternalId(6);
		
		Set<Node> neighborsOf1 = graphMock.getNeighborsOf(node1);
		Set<Node> neighborsOf2 = graphMock.getNeighborsOf(node2);
		Set<Node> neighborsOf3 = graphMock.getNeighborsOf(node3);
		Set<Node> neighborsOf4 = graphMock.getNeighborsOf(node4);
		Set<Node> neighborsOf5 = graphMock.getNeighborsOf(node5);
		Set<Node> neighborsOf6 = graphMock.getNeighborsOf(node6);
		
		Assert.assertEquals(neighborsOf1.size(), 3);
		Assert.assertTrue(neighborsOf1.contains(node2));
		Assert.assertTrue(neighborsOf1.contains(node3));
		Assert.assertTrue(neighborsOf1.contains(node4));
		
		Assert.assertEquals(neighborsOf2.size(), 3);
		Assert.assertTrue(neighborsOf2.contains(node3));
		Assert.assertTrue(neighborsOf2.contains(node4));
		Assert.assertTrue(neighborsOf2.contains(node5));
		
		Assert.assertEquals(neighborsOf3.size(), 2);
		Assert.assertTrue(neighborsOf3.contains(node4));
		Assert.assertTrue(neighborsOf3.contains(node5));
		
		Assert.assertEquals(neighborsOf4.size(), 1);
		Assert.assertTrue(neighborsOf4.contains(node6));
		
		Assert.assertEquals(neighborsOf5.size(), 1);
		Assert.assertTrue(neighborsOf5.contains(node6));
		
		Assert.assertEquals(neighborsOf6.size(), 0);
		
	}
	
}
