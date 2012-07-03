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
		Recommendation rec = new Recommendation();
		LeafDendrogram<Recommendation> leaf = new LeafDendrogram<Recommendation>(rec);
	}
	
	
	
	@Test
	public void getContainedObjectReturnsContainedObject() {
		Recommendation rec = new Recommendation();
		LeafDendrogram<Recommendation> leaf = new LeafDendrogram<Recommendation>(rec);
		assertTrue(leaf.object() == rec);
	}
	
	
	
	@Test
	public void memberSetReturnsContainedObject() {
		Recommendation rec = new Recommendation();
		LeafDendrogram<Recommendation> leaf = new LeafDendrogram<Recommendation>(rec);
		assertTrue(leaf.memberSet().contains(rec));
		assertTrue(leaf.memberSet().size() == 1);
	}
	
	
	
	@Test
	public void addMembersAddsContainedObjectToGivenSet() {
		HashSet<Recommendation> set = new HashSet<Recommendation>();
		Recommendation rec = new Recommendation();
		LeafDendrogram<Recommendation> leaf = new LeafDendrogram<Recommendation>(rec);
		leaf.addMembers(set);
		assertTrue(set.contains(rec));
		assertTrue(set.size() == 1);
	}
	
}
