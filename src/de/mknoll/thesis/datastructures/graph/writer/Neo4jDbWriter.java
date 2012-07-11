package de.mknoll.thesis.datastructures.graph.writer;


import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.rest.graphdb.RestAPI;

import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;

import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;



/**
 * Class implements a writer that stores graph into a Neo4j graph database.
 * 
 * @author Michael Knoll <mimi@kaktsuteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.writer.Neo4jWriterTest
 */
public class Neo4jDbWriter extends Neo4jWriter {
	
	/**
	 * Holds API for working with graph database
	 */
	RestAPI graphRestApi;
	
	
	
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
			
			this.createRelationship(
					sourceNode, 
					targetNode, 
					RecommenderRelationshipTypes.IS_RECOMMENDATION_FOR, 
					null
			);
		}
	}
	

	
	private void initializeRestApi(String destination) {
		this.graphRestApi = new RestAPI(destination);
	}
	
	
	
	/**
	 * Creates a node using rest API and returns created node
	 */
	protected Node createNode(Map<String, Object> properties) {
		Node n4jNode = this.graphRestApi.createNode(properties);
		return n4jNode;
	}
	
	
	
	/**
	 * Creates a relationship for given source, target, type and properties
	 */
	protected Relationship createRelationship(Node sourceNode, Node targetNode,
			 RecommenderRelationshipTypes type, Map<String,Object> properties) {
		Relationship relationship = this.graphRestApi.createRelationship(
				sourceNode, 
				targetNode, 
				type, 
				properties
		);
		return relationship;
	}

}
