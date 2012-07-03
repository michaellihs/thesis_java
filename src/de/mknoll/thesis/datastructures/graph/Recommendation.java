package de.mknoll.thesis.datastructures.graph;

import org.jgrapht.graph.DefaultWeightedEdge;



/**
 * Class implements a recommendation.
 * 
 * Within the graph of recommendations, a recommendation is an edge
 * between a source vertex and a target vertex. A recommendation is 
 * weighted (reflecting the quality of the recommendation) and
 * directed.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Recommendation extends DefaultWeightedEdge {
	
	/**
	 * Holds serial version ID
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * Returns weight of edge which is the quality of the recommendation
	 */
	public double getWeight() {
		return super.getWeight();
	}
	
	
	
	/**
	 * Returns source of recommendation
	 */
	public RecommenderObject getSourceRecommendation() {
		return this.getRecommenderObjectFromNode((Node)super.getSource());
	}
	
	
	
	/**
	 * Returns target of recommendation
	 */
	public RecommenderObject getTargetRecommendation() {
		return this.getRecommenderObjectFromNode((Node)super.getTarget());
	}
	
	
	
	/**
	 * Returns source node of this recommendation
	 */
	public Node getSource() {
		return (Node)super.getSource();
	}
	
	
	
	/**
	 * Returns target node of this recommendation
	 */
	public Node getTarget() {
		return (Node)super.getTarget();
	}
	
	
	
	private RecommenderObject getRecommenderObjectFromNode(Node node) {
		return (RecommenderObject) node.getAttachedObject(RecommenderObject.NODE_IDENTIFIER);
	}
	
}
