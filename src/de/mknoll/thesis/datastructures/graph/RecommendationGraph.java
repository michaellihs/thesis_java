package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;



/**
 * Class implements graph used for recommendations network.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.RecommendationGraphTest
 */
public class RecommendationGraph extends DefaultDirectedWeightedGraph<Node, Recommendation> {

	/**
	 * Holds serialization version ID
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Holds id node map
	 */
	private IdNodeMap idNodeMap;
	
	
	
	/**
	 * Holds a set of contained recommender objects
	 */
	private Map<String,RecommenderObject> containedRecommenderObjects = new HashMap<String,RecommenderObject>();
	
	
	
	/**
	 * Holds mapping internal id -> node
	 */
	private Map<Integer, Node> internalIdToNodeMapping = new HashMap<Integer, Node>();
	
	
	
	/**
	 * Constructor for 
	 */
	public RecommendationGraph(IdNodeMap idNodeMap) {
		super(Recommendation.class);
		this.idNodeMap = idNodeMap;
	}
	
	
	
	/**
	 * Adds a new recommendation (edge) for given source, target and weight.
	 * 
	 * @param source Source object of added recommendation
	 * @param target Target object of added recommendation
	 * @return Created edge, which had been added to recommendation graph
	 * @throws Exception If recommender objects do not have doc id set
	 */
	public Recommendation addRecommendation(RecommenderObject source, RecommenderObject target) throws Exception {
		return this.addRecommendation(source, target, RecommendationGraph.DEFAULT_EDGE_WEIGHT);
	}
	
	
	
	/**
	 * Adds a new recommendation (edge) for given source, target and weight.
	 * 
	 * @param source Source object of added recommendation
	 * @param target Target object of added recommendation
	 * @param weight Weight (quality) of recommendation
	 * @return Created edge, which had been added to recommendation graph
	 * @throws Exception If recommender objects do not have docId set
	 */
	public Recommendation addRecommendation(RecommenderObject source, RecommenderObject target, double weight) throws Exception {
		Node sourceNode, targetNode;
		if (!this.containsRecommenderObject(source)) {
			sourceNode = this.addRecommenderObject(source);
		} else {
			sourceNode = this.getNodeByRecommenderObject(source);
		}
		
		if (!this.containsRecommenderObject(target)) {
			targetNode = this.addRecommenderObject(target);
		} else {
			targetNode = this.getNodeByRecommenderObject(target);
		}
		
		if (sourceNode == null || targetNode == null) {
			throw new Exception("Error occured while trying to add edge for " + source.toString() + " -- " + target.toString());
		}
		
		Recommendation rec = this.addEdge(sourceNode, targetNode);
		
		if (rec == null) {
			// check whether we have duplicate edge --> get original edge
			rec = this.getEdge(sourceNode, targetNode);
			if (rec == null) {
				throw new Exception("Trying to add an edge that is null!");
			}
		} else {
			// only set edge weight on first edge, duplicates are not further processed
			this.setEdgeWeight(rec, weight);
		}
		
		return rec;
	}
	
	
	
	/**
	 * Returns node for given internal id
	 * 
	 * @param Internal id to look up node for
	 * @return Contained node for given internal id
	 */
	public Node getNodeByInternalId(int internalId) {
		return this.internalIdToNodeMapping.get(internalId);
	}
	
	
	
	/**
	 * Returns node by given recommender object
	 * 
	 * @param rec Recommender object to get node for
	 * @return Node for given recommender object
	 */
	public Node getNodeByRecommenderObject(RecommenderObject rec) {
		return this.idNodeMap.getNodeByNamespaceAndExternalId(DefaultNamespaces.BIBTIP, rec.getDocId());
	}
	
	
	
	/**
	 * Returns a set of all recommendations.
	 * 
	 * @return Set of all recommendations in this graph
	 */
	public Set<Recommendation> getAllRecommendations() {
		// TODO change behavior such that recommender objects are returned (atm, nodes are returned!)
		return this.edgeSet();
	}



	/**
	 * Adds given recommender object to recommendation graph
	 * 
	 * @param rec Recommender object to be added to graph
	 * @throws Exception If no docId is set on given recommender object
	 */
	public Node addRecommenderObject(RecommenderObject rec) throws Exception {
		/* What is to be done here?
		 *
		 * 1. Create new Node object for recommender object
		 * 2. Attach recommender object to node
		 * 3. Add node to vertices of this graph
		 */
		if (rec.hasEmptyDocId()) {
			throw new Exception("Given recommender object has empty docId and cannot be added to recommendation graph.");
		}
		Node node = new Node(this.idNodeMap);
		node.attachObject(rec);
		this.addVertex(node);
		this.containedRecommenderObjects.put(rec.getDocId(), rec);
		this.internalIdToNodeMapping.put(node.internalId(), node);
		return node;
	}



	/**
	 * Returns id to node mapping of this graph
	 * 
	 * @return
	 */
	public IdNodeMap getIdNodeMap() {
		return this.idNodeMap;
	}
	
	
	
	/**
	 * Returns all neighbors of given node
	 * 
	 * @param n
	 * @return Set of neighbors of given node
	 */
	public Set<Node> getNeighborsOf(Node n) {
		Set<Node> neighbors = new HashSet<Node>();
		for (Recommendation r : this.edgesOf(n)) {
			if (r.getTarget() != n) {
				neighbors.add(r.getTarget());
			}
		}
		return neighbors;
	}
	
	
	
	/**
	 * Returns a subgraph of this graph for a given node set
	 * 
	 * Use this function to get a subgraph e.g. for a maximum component
	 * of the graph generated by connectivity inspector.
	 *  
	 * @param nodeSet Node set to get subgraph for
	 * @return Subgraph for given node set
	 * @throws Exception 
	 */
	public RecommendationGraph getSubgraphForNodeSet(Set<Node> nodeSet) throws Exception {
		UniqueNodeIdProvider.getInstance().reset();
		RecommendationGraph g = new RecommendationGraph(new DefaultIdNodeMap());
		
		for (Node n : nodeSet) {
			RecommenderObject rObj1 = (RecommenderObject) n.getAttachedObject(RecommenderObject.NODE_IDENTIFIER);
			for (Node neighbor : this.getNeighborsOf(n)) {
				RecommenderObject rObj2 = (RecommenderObject) neighbor.getAttachedObject(RecommenderObject.NODE_IDENTIFIER);
				// TODO think about how to add weights here
				g.addRecommendation(rObj1, rObj2);
			}
		}
		
		return g;
	}



	/**
	 * Returns true, if given recommender object is contained within this graph
	 * 
	 * @param rec Recommender object to be checked whether it is contained by this graph
	 * @return True, if given recommender object is contained by this graph
	 */
	private boolean containsRecommenderObject(RecommenderObject rec) {
		return this.containedRecommenderObjects.containsKey(rec.getDocId());
	}

}
