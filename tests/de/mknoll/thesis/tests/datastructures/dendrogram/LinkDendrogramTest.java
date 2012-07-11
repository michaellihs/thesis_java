package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Testcase implements some tests for dendrogram implementation of this thesis
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class LinkDendrogramTest {
	
	@Test
	public void linkDendrogramCanBeConstructedForTwoGivenLeaves() {
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"));
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String2"));
		
		LinkDendrogram<RecommenderObject> link = new LinkDendrogram<RecommenderObject>(leaf1, leaf2);
	}
	
	
	
	@Test
	public void memberSetReturnsSetOfMembersOfLinkedDendrograms() {
		RecommenderObject rec1 = new RecommenderObject();
		RecommenderObject rec2 = new RecommenderObject();
		RecommenderObject rec3 = new RecommenderObject();
		
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(rec1);
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(rec2);
		LeafDendrogram<RecommenderObject> leaf3 = new LeafDendrogram<RecommenderObject>(rec3);
		
		LinkDendrogram<RecommenderObject> link1 = new LinkDendrogram<RecommenderObject>(leaf1, leaf2);
		LinkDendrogram<RecommenderObject> link2 = new LinkDendrogram<RecommenderObject>(link1, leaf3);
		
		Set<RecommenderObject> set = link2.memberSet();
		
		assertTrue(set.size() == 3);
		assertTrue(set.contains(rec1));
		assertTrue(set.contains(rec2));
		assertTrue(set.contains(rec3));
	}
	
	
	
	@Test
	public void parentIsSetInDendrogramWhenAddedToLinkDendrogram() {
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"));
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String2"));
		
		LinkDendrogram<RecommenderObject> link = new LinkDendrogram<RecommenderObject>(leaf1, leaf2);
		
		assertTrue(leaf1.parent() == link);
		assertTrue(leaf2.parent() == link);
	}
	
	
	
	@Test
	public void mostUpperParentIsReturnedWhenDereferencingDendrogram() {
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"), "leaf1");
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String2"), "leaf2");
		LeafDendrogram<RecommenderObject> leaf3 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String3"), "leaf3");
		LeafDendrogram<RecommenderObject> leaf4 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String4"), "leaf4");
		
		LinkDendrogram<RecommenderObject> link1 = new LinkDendrogram<RecommenderObject>(leaf1, leaf2, "link1");
		LinkDendrogram<RecommenderObject> link2 = new LinkDendrogram<RecommenderObject>(link1, leaf3, "link2");
		LinkDendrogram<RecommenderObject> link3 = new LinkDendrogram<RecommenderObject>(link2, leaf4, "link3");
		
		assertTrue(leaf1.dereference() == link3);
		assertTrue(leaf2.dereference() == link3);
		assertTrue(leaf3.dereference() == link3);
		assertTrue(leaf4.dereference() == link3);
		
		assertTrue(link1.dereference() == link3);
		assertTrue(link2.dereference() == link3);
		assertTrue(link3.dereference() == link3);
	}
	
	
	
	@Test
	public void partitionThrowsExceptionOnWrongParameter() {
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"));
		try {
			leaf.partition(1);
			fail("Expected exception because of wrong parameter, but no exception has been thrown");
		} catch (Exception e) {
			return;
		}
	}
	
	
	
	@Test
	public void partitionThrowsExceptionIfNumberOfPartitionsIsBiggerThenContainedElements() {
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"));
		try {
			leaf.partition(2);
			fail("Expected exception because number of partitions was bigger than contained elements, but no exception has been thrown!");
		} catch (Exception e) {
			return;
		}
	}
	
	
	
	@Test
	public void partitionReturnsCorrectSetOfPartitions() {
		// TODO fix me!
		/*
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String1"), "leaf1");
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String2"), "leaf2");
		LeafDendrogram<RecommenderObject> leaf3 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String3"), "leaf3");
		LeafDendrogram<RecommenderObject> leaf4 = new LeafDendrogram<RecommenderObject>(new RecommenderObject("String4"), "leaf4");
		
		LinkDendrogram<RecommenderObject> link1 = new LinkDendrogram<RecommenderObject>(leaf1, leaf2, "link1");
		LinkDendrogram<RecommenderObject> link2 = new LinkDendrogram<RecommenderObject>(link1, leaf3, "link2");
		LinkDendrogram<RecommenderObject> link3 = new LinkDendrogram<RecommenderObject>(link2, leaf4, "link3");
		
		Set<Set<RecommenderObject>> resultSet = link3.partition(2);
		assertTrue(resultSet.size() == 2);
		
		// TODO woohoo - this is a rather lazy assertion...
		assertTrue(resultSet.toString().equals("[[String4], [String3, String1, String2]]"));
		*/
	}
	
}
