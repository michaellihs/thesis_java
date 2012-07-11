package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.annotations.Inject;
import org.picocontainer.injectors.MultiInjection;
import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.EdgeListWriter;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.tests.testclasses.*;



/**
 * Testcase implements some tests for dendrogram implementation of this thesis
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class LeafDendrogramTest {
	
	@Test
	public void LeafDendrogramCanBeCreatedForArbitraryObject() {
		RecommenderObject recObj = new RecommenderObject();
		LeafDendrogram<RecommenderObject> leaf = new LeafDendrogram<RecommenderObject>(recObj);
	}
	
	
	
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
	
}
