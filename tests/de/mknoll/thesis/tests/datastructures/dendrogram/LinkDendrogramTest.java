package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.Recommendation;



/**
 * Testcase implements some tests for dendrogram implementation of this thesis
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class LinkDendrogramTest {
	
	@Test
	public void linkDendrogramCanBeConstructedForTwoGivenLeaves() {
		LeafDendrogram<String> leaf1 = new LeafDendrogram<String>("String1");
		LeafDendrogram<String> leaf2 = new LeafDendrogram<String>("String2");
		
		LinkDendrogram<String> link = new LinkDendrogram<String>(leaf1, leaf2);
	}
	
	
	
	@Test
	public void memberSetReturnsSetOfMembersOfLinkedDendrograms() {
		Recommendation rec1 = new Recommendation();
		Recommendation rec2 = new Recommendation();
		Recommendation rec3 = new Recommendation();
		
		LeafDendrogram<Recommendation> leaf1 = new LeafDendrogram<Recommendation>(rec1);
		LeafDendrogram<Recommendation> leaf2 = new LeafDendrogram<Recommendation>(rec2);
		LeafDendrogram<Recommendation> leaf3 = new LeafDendrogram<Recommendation>(rec3);
		
		LinkDendrogram<Recommendation> link1 = new LinkDendrogram<Recommendation>(leaf1, leaf2);
		LinkDendrogram<Recommendation> link2 = new LinkDendrogram<Recommendation>(link1, leaf3);
		
		Set<Recommendation> set = link2.memberSet();
		
		assertTrue(set.size() == 3);
		assertTrue(set.contains(rec1));
		assertTrue(set.contains(rec2));
		assertTrue(set.contains(rec3));
	}
	
	
	
	@Test
	public void parentIsSetInDendrogramWhenAddedToLinkDendrogram() {
		LeafDendrogram<String> leaf1 = new LeafDendrogram<String>("String1");
		LeafDendrogram<String> leaf2 = new LeafDendrogram<String>("String2");
		
		LinkDendrogram<String> link = new LinkDendrogram<String>(leaf1, leaf2);
		
		assertTrue(leaf1.parent() == link);
		assertTrue(leaf2.parent() == link);
	}
	
	
	
	@Test
	public void mostUpperParentIsReturnedWhenDereferencingDendrogram() {
		LeafDendrogram<String> leaf1 = new LeafDendrogram<String>("String1", "leaf1");
		LeafDendrogram<String> leaf2 = new LeafDendrogram<String>("String2", "leaf2");
		LeafDendrogram<String> leaf3 = new LeafDendrogram<String>("String3", "leaf3");
		LeafDendrogram<String> leaf4 = new LeafDendrogram<String>("String4", "leaf4");
		
		LinkDendrogram<String> link1 = new LinkDendrogram<String>(leaf1, leaf2, "link1");
		LinkDendrogram<String> link2 = new LinkDendrogram<String>(link1, leaf3, "link2");
		LinkDendrogram<String> link3 = new LinkDendrogram<String>(link2, leaf4, "link3");
		
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
		LeafDendrogram<String> leaf = new LeafDendrogram<String>("String1");
		try {
			leaf.partition(1);
			fail("Expected exception because of wrong parameter, but no exception has been thrown");
		} catch (Exception e) {
			return;
		}
	}
	
	
	
	@Test
	public void partitionThrowsExceptionIfNumberOfPartitionsIsBiggerThenContainedElements() {
		LeafDendrogram<String> leaf = new LeafDendrogram<String>("String1");
		try {
			leaf.partition(2);
			fail("Expected exception because number of partitions was bigger than contained elements, but no exception has been thrown!");
		} catch (Exception e) {
			return;
		}
	}
	
	
	
	@Test
	public void partitionReturnsCorrectSetOfPartitions() {
		LeafDendrogram<String> leaf1 = new LeafDendrogram<String>("String1", "leaf1");
		LeafDendrogram<String> leaf2 = new LeafDendrogram<String>("String2", "leaf2");
		LeafDendrogram<String> leaf3 = new LeafDendrogram<String>("String3", "leaf3");
		LeafDendrogram<String> leaf4 = new LeafDendrogram<String>("String4", "leaf4");
		
		LinkDendrogram<String> link1 = new LinkDendrogram<String>(leaf1, leaf2, "link1");
		LinkDendrogram<String> link2 = new LinkDendrogram<String>(link1, leaf3, "link2");
		LinkDendrogram<String> link3 = new LinkDendrogram<String>(link2, leaf4, "link3");
		
		Set<Set<String>> resultSet = link3.partition(2);
		assertTrue(resultSet.size() == 2);
		
		// TODO woohoo - this is a rather lazy assertion...
		assertTrue(resultSet.toString().equals("[[String4], [String3, String1, String2]]"));
	}
	
}
