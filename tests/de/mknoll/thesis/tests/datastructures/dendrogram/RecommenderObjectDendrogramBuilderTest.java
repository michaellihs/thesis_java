package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.RecommenderObjectDendrogramBuilder;
import de.mknoll.thesis.datastructures.graph.IdentifierRecommenderObjectMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Testcase for recommender object dendrogram builder 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.dendrogram.DendrogramBuilder
 */
public class RecommenderObjectDendrogramBuilderTest {
	
	private IdentifierRecommenerObjectMapMock mapMock;
	
	
	
	@Test
	public void dendrogramIsBuildUpCorrectlyForGivenSampleData() {
		this.mapMock = new IdentifierRecommenerObjectMapMock();
		RecommenderObjectDendrogramBuilder builder = new RecommenderObjectDendrogramBuilder(this.mapMock);
		builder.mergeByIds("1", "2");
		builder.mergeByIds("1", "3");
		builder.mergeByIds("4", "5");
		builder.mergeByIds("1", "4");
		try {
			
			LinkDendrogram<RecommenderObject> dendrogram = builder.getDendrogram();
			assertTrue(dendrogram.getClass() == LinkDendrogram.class);
			
			LinkDendrogram<RecommenderObject> link123 = (LinkDendrogram<RecommenderObject>)dendrogram.dendrogram1();
			LinkDendrogram<RecommenderObject> link45  = (LinkDendrogram<RecommenderObject>)dendrogram.dendrogram2();
			
			LinkDendrogram<RecommenderObject> link12  = (LinkDendrogram<RecommenderObject>)link123.dendrogram1();
			
			LeafDendrogram<RecommenderObject> leaf3   = (LeafDendrogram<RecommenderObject>)link123.dendrogram2();
			assertTrue(leaf3.object().getDocId() == "3");
			
			LeafDendrogram<RecommenderObject> leaf1   = (LeafDendrogram<RecommenderObject>)link12.dendrogram1();
			assertTrue(leaf1.object().getDocId() == "1");
			
			LeafDendrogram<RecommenderObject> leaf2   = (LeafDendrogram<RecommenderObject>)link12.dendrogram2();
			assertTrue(leaf2.object().getDocId() == "2");
			
			LeafDendrogram<RecommenderObject> leaf4   = (LeafDendrogram<RecommenderObject>)link45.dendrogram1();
			assertTrue(leaf4.object().getDocId() == "4");
			
			LeafDendrogram<RecommenderObject> leaf5   = (LeafDendrogram<RecommenderObject>)link45.dendrogram2();
			assertTrue(leaf5.object().getDocId() == "5");
			
		} catch (Exception e) {
			fail("dendrogram builder threw an exception although there shouldn't have been one!");
		}
	}
	
	
	
	@Test
	public void getDendrogramThrowsExceptionOnUnifinishedDendrogram() {
		this.mapMock = new IdentifierRecommenerObjectMapMock();
		RecommenderObjectDendrogramBuilder builder = new RecommenderObjectDendrogramBuilder(this.mapMock);
		builder.mergeByIds("1", "2");
		builder.mergeByIds("1", "3");
		builder.mergeByIds("4", "5");
		try {
			LinkDendrogram<RecommenderObject> dendrogram = builder.getDendrogram();
			fail("No exception has been thrown when trying to get unfinished dendrogram from builder!");
		} catch (Exception e) {
			// Nothing to do here - exception has been thrown as we expected!
		}
	}
	
	
	
	/**
	 * Mock class for testing
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class IdentifierRecommenerObjectMapMock extends IdentifierRecommenderObjectMap {
		
		public RecommenderObject getRecommenderObjectByExternalId(String id) {
			return new RecommenderObject(id);
		}
		
		
		public String getExternalIdByRecommenderObject(RecommenderObject recObject) {
			return recObject.getDocId();
		}
		
	}
	
}
