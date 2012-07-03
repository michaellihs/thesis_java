package de.mknoll.thesis.datastructures.graph.writer;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestAPI;
import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;

import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;



/**
 * Class implements a writer that stores graph into a Neo4j graph database.
 * 
 * @author Michael Knoll <mimi@kaktsuteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.writer.Neo4jWriterTest
 */
public class Neo4jWriter implements GraphWriter {
	
	/**
	 * Holds configuration for writer
	 */
	@Inject private HashMap<String, String> writerConfiguration;
	
	
	
	/**
	 * Holds mapping of already inserted nodes
	 */
	private Map<String, Node> neo4jIdToNodeMapping = new HashMap<String, Node>();

	
	
	/**
	 * Holds API for working with graph database
	 */
	private RestAPI graphRestApi;



	/**
	 * Holds recommendation graph currently written to database
	 */
	private RecommendationGraph recommendationGraph;
	
	
	
	/**
	 * Writes given graph into Neo4j database.
	 * @throws Exception 
	 * @Override
	 */
	public void write(RecommendationGraph graph, String destination) throws Exception {
		this.recommendationGraph = graph;
		this.initializeRestApi(destination);
		
		for(Recommendation rec: graph.getAllRecommendations()) {
			Node sourceNode = this.insertNode(rec.getSource());
			Node targetNode = this.insertNode(rec.getTarget());
			
			this.graphRestApi.createRelationship(
					sourceNode, 
					targetNode, 
					RecommenderRelationshipTypes.IS_RECOMMENDATION_FOR, 
					null
			);
		}
	}
	

	
	private Node insertNode(de.mknoll.thesis.datastructures.graph.Node node) throws Exception {
		// Check whether node has been inserted into neo4j db yet (--> node has neo4j id)
		if (node.hasExternalId(DefaultNamespaces.NEO4J)) {
			// return already inserted node
			return this.getInsertedNode(node);
		} else {
			// insert new node into neo4j db and return instance of inserted node
			return this.createAndInsertNewNode(node);
		}
	}



	private Node getInsertedNode(de.mknoll.thesis.datastructures.graph.Node node) {
		return this.neo4jIdToNodeMapping.get(node.getExternalIdByNamespace(DefaultNamespaces.NEO4J));
	}



	private Node createAndInsertNewNode(de.mknoll.thesis.datastructures.graph.Node node) throws Exception {
		Map<String, Object> properties = node.getFlatPropertiesMap();
		properties.put("rec_count", this.recommendationGraph.outDegreeOf(node));
		try {
			Node n4jNode = this.graphRestApi.createNode(properties);
			node.addExternalId(DefaultNamespaces.NEO4J, new Long(n4jNode.getId()).toString());
			this.neo4jIdToNodeMapping.put(new Long(n4jNode.getId()).toString(), n4jNode);
			return n4jNode;
		} catch (Exception e) {
			Exception newE = new Exception(e.getMessage() + "\n Trying to set Properties: \n " + properties.toString());
			newE.setStackTrace(e.getStackTrace());
			throw newE;
		}
	}



	private void initializeRestApi(String destination) {
		this.graphRestApi = new RestAPI(destination);
	}

}
