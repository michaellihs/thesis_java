package de.mknoll.thesis.tests.analysis;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import de.mknoll.thesis.analysis.ModularityAnalyzer;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.UniqueNodeIdProvider;



/**
 * Class implements testcase for modularity analyzer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.analysis.ModularityAnalyzer
 */
public class ModularityAnalyzerTest {
	
	private static final double EPSILON = 0.0000000000001;



	private RecommenderObject[] nodes = new RecommenderObject[5];
	
	
	
	private DefaultIdNodeMap idNodeMap;

	
	
	@Test
	public void modularityReturnsExpectedValueForFirstPartioning() throws Exception {
		RecommendationGraph graph = this.createSampleRecommendationGraph();
		HashSet<HashSet<Node>> partitioning = this.createFirstPartitioning();
		ModularityAnalyzer modularityAnalyzer = new ModularityAnalyzer(graph);
		Double modularity = modularityAnalyzer.modularity(partitioning);
		Assert.assertTrue((modularity - (-(1/72))) < EPSILON);
	}
	
	
	
	@Test
	public void modularityReturnsExpectedValueForSecondPartitioning() throws Exception {
		RecommendationGraph graph = this.createSampleRecommendationGraph();
		HashSet<HashSet<Node>> partitioning = this.createSecondPartitioning();
		ModularityAnalyzer modularityAnalyzer = new ModularityAnalyzer(graph);
		Double modularity = modularityAnalyzer.modularity(partitioning);
		Assert.assertTrue((modularity - (1/9)) < EPSILON);
	}

	
	
	private HashSet<HashSet<Node>> createSecondPartitioning() {
		HashSet<HashSet<Node>> partitioning2 = new HashSet<HashSet<Node>>();
		HashSet<Node> partition1 = new HashSet<Node>();
		partition1.add(this.nodes[0].getNode());
		partition1.add(this.nodes[1].getNode());
		partition1.add(this.nodes[2].getNode());
		
		HashSet<Node> partition2 = new HashSet<Node>();
		partition2.add(this.nodes[3].getNode());
		partition2.add(this.nodes[4].getNode());
		
		partitioning2.add(partition1);
		partitioning2.add(partition2);
		
		return partitioning2;
	}



	private HashSet<HashSet<Node>> createFirstPartitioning() {
		HashSet<HashSet<Node>> partitioning1 = new HashSet<HashSet<Node>>();
		HashSet<Node> partition1 = new HashSet<Node>();
		partition1.add(this.nodes[0].getNode());
		partition1.add(this.nodes[1].getNode());
		
		HashSet<Node> partition2 = new HashSet<Node>();
		partition2.add(this.nodes[2].getNode());
		partition2.add(this.nodes[3].getNode());
		partition2.add(this.nodes[4].getNode());
		
		partitioning1.add(partition1);
		partitioning1.add(partition2);
		
		return partitioning1;
	}



	private RecommendationGraph createSampleRecommendationGraph() throws Exception {
		UniqueNodeIdProvider.getInstance().reset();
		this.idNodeMap = new DefaultIdNodeMap();
		this.createRecommenderObjects();
		RecommendationGraph graph = new RecommendationGraph(this.idNodeMap);
		for (int i = 0; i < 5; i++) {
			graph.addRecommenderObject(this.nodes[i]);
		}
		graph.addRecommendation(this.nodes[0], this.nodes[1]);
		graph.addRecommendation(this.nodes[0], this.nodes[2]);
		graph.addRecommendation(this.nodes[1], this.nodes[2]);
		graph.addRecommendation(this.nodes[1], this.nodes[4]);
		graph.addRecommendation(this.nodes[2], this.nodes[3]);
		graph.addRecommendation(this.nodes[3], this.nodes[4]);
		return graph;
	}



	private void createRecommenderObjects() {
		for (int i = 0; i < 5; i++) {
			this.nodes[i] = new RecommenderObject("docId" + i);
		}
	}
	
}
