package de.mknoll.thesis.datastructures.graph.inspectors;

import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;

import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Class implements a connectivity inspector that detects
 * components of a graph
 * 
 * TODO if required: cache results --> see caching of parent class
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class RecommendationGraphConnectivityInspector extends
		ConnectivityInspector<Node, Recommendation> {
	
	/**
	 * Holds accessible instance of inspected graph (graph in parent class is private)
	 */
	protected RecommendationGraph inspectedGraph;
	
	
	
	/**
	 * Constructor takes graph to be inspected as argument
	 * 
	 * @param g
	 */
	public RecommendationGraphConnectivityInspector(
			RecommendationGraph g) {
		super(g);
		this.inspectedGraph = g;
	}
	
	
	
	/**
	 * Returns biggest component of inspected graph
	 * 
	 * @return Biggest component of inspected graph
	 */
	public Set<Node> biggestComponent() {
		List<Set<Node>> components = super.connectedSets();
		Set<Node> biggestComponent = components.get(0);
		for (Set<Node> component : components) {
			if (component.size() > biggestComponent.size()) {
				biggestComponent = component;
			}
		}
		return biggestComponent;
	}
	
	
	
	/**
	 * Returns a subgraph of inspected recommendation graph for biggest component
	 * 
	 * @return
	 * @throws Exception
	 */
	public RecommendationGraph biggestComponentAsSubgraph() throws Exception {
		return this.inspectedGraph.getSubgraphForNodeSet(this.biggestComponent());
	}

}
