package de.mknoll.thesis.datastructures.tagcloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements comparison of tag clouds within a given
 * dendrogram.
 * 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.DendrogramTagCloudComparatorTest
 */
public class DendrogramTagCloudComparator {
	
	/**
	 * Holds dendrogram to be analyzed
	 */
	private Dendrogram<RecommenderObject> dendrogram;
	
	
	
	/**
	 * Holds a map of tag cloud comparators to be used for dendrogram tag cloud comparison
	 */
	private Map<String, TagCloudComparator> comparatorMap;
	
	
	
	/**
	 * Holds linked list of nodes that still need to be analyzed
	 */
	private LinkedList<Dendrogram<RecommenderObject>> nodeQueue;
	
	
	
	/**
	 * Holds a set of nodes that either has already been analyzed
	 * or has been added to list of nodes to be analyzed and hence
	 * need not to be added any more.
	 */
	private Set<Dendrogram<RecommenderObject>> touchedNodes;
	
	

	/**
	 * Constructor takes dendrogram to be analyzed as dependency
	 * 
	 * @param dendrogram Dendrogram to be analyzed
	 */
	public DendrogramTagCloudComparator(Dendrogram<RecommenderObject> dendrogram) {
		this.comparatorMap = new HashMap<String, TagCloudComparator>();
		this.dendrogram = dendrogram;
	}



	/**
	 * Adds given tag cloud comparator using given comparator name to list of comparators to be
	 * used for tag cloud comparison in dendrogram.
	 * 
	 * @param comparatorName Name of comparator to be added
	 * @param tagCloudComparator Instance of comparator to be added
	 */
	public void addComparator(String comparatorName, TagCloudComparator tagCloudComparator) {
		this.comparatorMap.put(comparatorName, tagCloudComparator);
	}



	public void runComparison() {
		this.initializeNodeQueue();
		while (!this.nodeQueue.isEmpty()) {
			Dendrogram<RecommenderObject> currentNode = this.nodeQueue.removeFirst();
			this.analyzeNode(currentNode);
		}
	}



	private void analyzeNode(Dendrogram<RecommenderObject> currentNode) {
		if (currentNode.hasParent()) {
			if (!this.touchedNodes.contains(currentNode.parent())) {
				this.touchedNodes.add(currentNode.parent());
				this.nodeQueue.add(currentNode.parent());
			}
			for (String comparatorName : this.comparatorMap.keySet()) {
				TagCloudComparator tagCloudComparator = this.comparatorMap.get(comparatorName);
				Double similarity = tagCloudComparator.compare(currentNode.tagCloud(), currentNode.parent().tagCloud());
				currentNode.setAdditionalValue(comparatorName, similarity);
				//if (similarity > 0.0) {
				//	System.out.println("Similarity > 0.0!");
				//	System.out.println("Setting node value " + comparatorName + " with value " + similarity.toString());
				//}
			}
		}
	}



	private void initializeNodeQueue() {
		this.touchedNodes = new HashSet<Dendrogram<RecommenderObject>>();
		this.nodeQueue = new LinkedList<Dendrogram<RecommenderObject>>();
		for (Dendrogram<RecommenderObject> d : this.dendrogram.leaves()) {
			this.nodeQueue.add(d);
			this.touchedNodes.add(d);
		}
	}
	

}
