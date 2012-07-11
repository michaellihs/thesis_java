package de.mknoll.thesis.datastructures.graph.writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Class implements an abstract base class for neo4j writers
 * 
 * We have n4j writers that write into a (running) database server
 * and others that write into an neo4j file. Methods shared by both
 * writers are put into this class.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 *
 */
public abstract class Neo4jWriter implements GraphWriter {

	/**
	 * Holds mapping of already inserted nodes
	 */
	protected Map<String, Node> neo4jIdToNodeMapping = new HashMap<String, Node>();



	/**
	 * Holds recommendation graph currently written to database
	 */
	protected RecommendationGraph recommendationGraph;
	
	

	public Neo4jWriter() {
		super();
	}
	
	

	protected Node insertNode(de.mknoll.thesis.datastructures.graph.Node node) throws Exception {
		// Check whether node has been inserted into neo4j db yet (--> node has neo4j id)
		if (node.hasExternalId(DefaultNamespaces.NEO4J)) {
			// return already inserted node
			return this.getInsertedNode(node);
		} else {
			// insert new node into neo4j db and return instance of inserted node
			return this.createAndInsertNewNode(node);
		}
	}
	
	

	protected Node getInsertedNode(de.mknoll.thesis.datastructures.graph.Node node) {
		return this.neo4jIdToNodeMapping.get(node.getExternalIdByNamespace(DefaultNamespaces.NEO4J));
	}
	
	

	protected Node createAndInsertNewNode(de.mknoll.thesis.datastructures.graph.Node node) throws Exception {
		Map<String, Object> properties = node.getFlatPropertiesMap();
		properties.put("rec_count", this.recommendationGraph.outDegreeOf(node));
		try {
			Node n4jNode = this.createNode(properties);
			node.addExternalId(DefaultNamespaces.NEO4J, new Long(n4jNode.getId()).toString());
			this.neo4jIdToNodeMapping.put(new Long(n4jNode.getId()).toString(), n4jNode);
			return n4jNode;
		} catch (Exception e) {
			Exception newE = new Exception(e.getMessage() + "\n Trying to set Properties: \n " + properties.toString());
			newE.setStackTrace(e.getStackTrace());
			throw newE;
		}
	}
	
	
	
	/**
	 * Creates a node for given properties and returns created node
	 * 
	 * @param properties
	 * @return
	 */
	abstract protected Node createNode(Map<String, Object> properties);
	
	
	
	/**
	 * Creates a relationship for given source node and given target node and given properties
	 * 
	 * @param sourceNode
	 * @param targetNode
	 * @param type
	 * @param properties
	 * @return
	 */
	abstract protected Relationship createRelationship(Node sourceNode, Node targetNode,
			 RecommenderRelationshipTypes type, Map<String,Object> properties);

}