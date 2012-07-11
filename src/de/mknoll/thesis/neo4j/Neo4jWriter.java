package de.mknoll.thesis.neo4j;

import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;



/**
 * Interface for classes that write nodes and relationship to Neo4J graph database-
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface Neo4jWriter {

	/**
	 * Creates neo4j node for given properties. 
	 * Returns created n4j node.
	 * 
	 * @param properties Properties to be set on node in n4j
	 * @return Created n4j node
	 */
	public Node createNode(Map<String, Object> properties);
	
	
	
	/**
	 * Creates neo4j relationship for given source and target node adding given properties to this relationship.
	 * Returns created n4j relationship
	 * 
	 * @param sourceNode Neo4J node to start relationship
	 * @param targetNode Neo4J node to end relationship
	 * @param type Type of relationship
	 * @param properties Properties of relationship
	 * @return Created Neo4J relationship
	 */
	public Relationship createRelationship(Node sourceNode, Node targetNode, 
			RecommenderRelationshipTypes type, Map<String,Object> properties);
	
	
	
	/**
	 * Starts a new transaction
	 */
	public void beginTransaction();
	
	
	
	/**
	 * Calls tx.success()
	 * @throws Exception 
	 */
	public void successTransaction() throws Exception;
	
	
	
	/**
	 * Finishes transaction
	 * @throws Exception 
	 */
	public void finishTransaction() throws Exception;
	
}
