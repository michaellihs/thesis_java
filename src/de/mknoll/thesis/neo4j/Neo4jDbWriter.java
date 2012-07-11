package de.mknoll.thesis.neo4j;

import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.rest.graphdb.RestAPI;

import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;



/**
 * Class implements a Neo4J storage for nodes and relationships using REST API of 
 * a running Neo4J database server given as URL.
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Neo4jDbWriter implements Neo4jWriter {
	
	/**
	 * Holds API for working with graph database
	 */
	private RestAPI graphRestApi;
	
	
	
	/**
	 * Constructor for getting n4jDbWriter by database url
	 * 
	 * @param url URL to n4j database (e.g. http://localhost:7474/db/data/)
	 */
	public Neo4jDbWriter(String url) {
		this.graphRestApi = new RestAPI(url);
	}
	
	
	
	/**
	 * Constructor for getting n4jDbWriter by graph REST API object
	 * 
	 * @param graphRestApi Graph REST API object that handles connection to database
	 */
	public Neo4jDbWriter(RestAPI graphRestApi) {
		this.graphRestApi = graphRestApi;
	}
	
	

	@Override
	public Node createNode(Map<String, Object> properties) {
		Node n4jNode = this.graphRestApi.createNode(properties);
		return n4jNode;
	}
	
	

	@Override
	public Relationship createRelationship(Node sourceNode, Node targetNode,
			RecommenderRelationshipTypes type, Map<String, Object> properties) {
		Relationship relationship = this.graphRestApi.createRelationship(
				sourceNode, 
				targetNode, 
				type, 
				properties
		);
		return relationship;
	}

}
