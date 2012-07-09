package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.NewmanDendrogramBuilder;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.UniqueNodeIdProvider;



/**
 * Testcase for recommender object dendrogram builder 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.dendrogram.DendrogramBuilder
 */
public class RecommenderObjectDendrogramBuilderTest {
	
	@Test
	public void dendrogramIsBuildUpCorrectlyForGivenSampleData() throws Exception {
		NewmanDendrogramBuilder builder = new NewmanDendrogramBuilder(this.getIdNodeMapMock());
		builder.mergeByIds("2", "1");
		builder.mergeByIds("3", "1");
		builder.mergeByIds("5", "4");
		builder.mergeByIds("4", "1");
		try {
			
			LinkDendrogram<RecommenderObject> dendrogram = builder.getDendrogram();
			assertTrue(dendrogram.getClass() == LinkDendrogram.class);
			
			LinkDendrogram<RecommenderObject> link123 = (LinkDendrogram<RecommenderObject>)dendrogram.dendrogram2();
			LinkDendrogram<RecommenderObject> link45  = (LinkDendrogram<RecommenderObject>)dendrogram.dendrogram1();
			
			LinkDendrogram<RecommenderObject> link12  = (LinkDendrogram<RecommenderObject>)link123.dendrogram2();
			
			LeafDendrogram<RecommenderObject> leaf3   = (LeafDendrogram<RecommenderObject>)link123.dendrogram1();
			assertTrue(leaf3.object().getDocId() == "3");
			
			LeafDendrogram<RecommenderObject> leaf1   = (LeafDendrogram<RecommenderObject>)link12.dendrogram2();
			assertTrue(leaf1.object().getDocId() == "1");
			
			LeafDendrogram<RecommenderObject> leaf2   = (LeafDendrogram<RecommenderObject>)link12.dendrogram1();
			assertTrue(leaf2.object().getDocId() == "2");
			
			LeafDendrogram<RecommenderObject> leaf4   = (LeafDendrogram<RecommenderObject>)link45.dendrogram2();
			assertTrue(leaf4.object().getDocId() == "4");
			
			LeafDendrogram<RecommenderObject> leaf5   = (LeafDendrogram<RecommenderObject>)link45.dendrogram1();
			assertTrue(leaf5.object().getDocId() == "5");
			
		} catch (Exception e) {
			fail("dendrogram builder threw an exception although there shouldn't have been one!");
		}
	}
	
	
	
	@Test
	public void getDendrogramThrowsExceptionOnUnifinishedDendrogram() throws Exception {
		NewmanDendrogramBuilder builder = new NewmanDendrogramBuilder(this.getIdNodeMapMock());
		builder.mergeByIds("2", "1");
		builder.mergeByIds("3", "1");
		builder.mergeByIds("5", "4");
		try {
			LinkDendrogram<RecommenderObject> dendrogram = builder.getDendrogram();
			fail("No exception has been thrown when trying to get unfinished dendrogram from builder!");
		} catch (Exception e) {
			// Nothing to do here - exception has been thrown as we expected!
		}
	}
	
	
	
	private IdNodeMap getIdNodeMapMock() {
		UniqueNodeIdProvider.getInstance().reset();
		IdNodeMap map = new DefaultIdNodeMap();
		
		Node node1 = new Node(map);
		map.createMappingForNode(node1);
		node1.attachObject(new RecommenderObject("1"));
		map.addExternalId(node1, DefaultNamespaces.BIBTIP, "1");
		
		Node node2 = new Node(map);
		map.createMappingForNode(node2);
		node2.attachObject(new RecommenderObject("2"));
		map.addExternalId(node2, DefaultNamespaces.BIBTIP, "2");
		
		Node node3 = new Node(map);
		map.createMappingForNode(node3);
		node3.attachObject(new RecommenderObject("3"));
		map.addExternalId(node3, DefaultNamespaces.BIBTIP, "3");
		
		Node node4 = new Node(map);
		map.createMappingForNode(node4);
		node4.attachObject(new RecommenderObject("4"));
		map.addExternalId(node4, DefaultNamespaces.BIBTIP, "4");
		
		Node node5 = new Node(map);
		map.createMappingForNode(node5);
		node5.attachObject(new RecommenderObject("5"));
		map.addExternalId(node5, DefaultNamespaces.BIBTIP, "5");
		
		return map;
	}
	
}
