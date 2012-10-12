package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Testcase implements some tests for dendrogram implementation of this thesis
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram
 */
public class LeafDendrogramTest {
	
	@Test
	public void getContainedObjectReturnsContainedObject() {
		RecommenderObject recObj = new RecommenderObject();
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(recObj);
		assertTrue(leaf.object() == recObj);
	}
	
	
	
	@Test
	public void memberSetReturnsContainedObject() {
		RecommenderObject recObj = new RecommenderObject();
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(recObj);
		assertTrue(leaf.memberSet().contains(recObj));
		assertTrue(leaf.memberSet().size() == 1);
	}
	
	
	
	@Test
	public void addMembersAddsContainedObjectToGivenSet() {
		HashSet<RecommenderObject> set = new HashSet<RecommenderObject>();
		RecommenderObject recObj = new RecommenderObject();
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(recObj);
		leaf.addMembers(set);
		assertTrue(set.contains(recObj));
		assertTrue(set.size() == 1);
	}
	
	
	
	@Test
	public void tagCloudContainsExpectedTags() {
		RecommenderObject recObj = new RecommenderObject("id:1234", "tag1 tag2 tag3 tag4");
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(recObj);
		Cloud c = leaf.tagCloud();
		Assert.assertTrue(c.containsName("tag1"));
		Assert.assertTrue(c.containsName("tag2"));
		Assert.assertTrue(c.containsName("tag3"));
	}
	
}
