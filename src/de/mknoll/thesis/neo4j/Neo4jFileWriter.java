package de.mknoll.thesis.neo4j;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;



/**
 * Class implements a Neo4J storage for nodes and relationships using 
 * direct access to Neo4J database files.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Neo4jFileWriter implements Neo4jWriter {
	
	/**
	 * Holds a connection to graph database
	 */
	private GraphDatabaseService graphDb;
	
	
	
	/**
	 * Holds current neo4j transaction
	 */
	private Transaction currentTransaction = null;
	
	
	
	/**
	 * Holds configuration properties path for neo4j database
	 */
	private String pathToConfig = "/Applications/Utilities/neo4j-advanced-1.8-SNAPSHOT/conf/";
	
	
	
	/**
	 * Constructor returns Neo4jFileWriter for given database path 
	 * using default configuration.
	 * 
	 * @param pathToDatabase Path (without filename) of neo4j files which writer should use as graph db
	 */
	public Neo4jFileWriter(String pathToDatabase) {
		this.graphDb = new GraphDatabaseFactory() .
				newEmbeddedDatabaseBuilder(pathToDatabase) .
				loadPropertiesFromFile(this.pathToConfig + "neo4j.properties") .
				newGraphDatabase();
	}
	
	
	
	/**
	 * Constructor returns Neo4jFileWriter using given graphDbService
	 * as storage.
	 * 
	 * @param graphDbService Graph database service used as storage
	 */
	public Neo4jFileWriter(GraphDatabaseService graphDbService) {
		this.graphDb = graphDbService;
	}
	
	

	@Override
	public Node createNode(Map<String, Object> properties) {
		Node createdNode = this.graphDb.createNode();
		if (properties != null) {
			for (String key : properties.keySet()) {
				createdNode.setProperty(key, properties.get(key));
			}
		}
		return createdNode;
	}
	
	

	@Override
	public Relationship createRelationship(Node sourceNode, Node targetNode,
			RecommenderRelationshipTypes type, Map<String, Object> properties) {

		Relationship relationship = sourceNode.createRelationshipTo(targetNode, type);
		if (properties != null) {
			for (String key: properties.keySet()) {
				relationship.setProperty(key, properties.get(key));
			}
		}
		return relationship;
	}



	@Override
	public void beginTransaction() {
		this.currentTransaction = this.graphDb.beginTx();
	}



	@Override
	public void finishTransaction() throws Exception {
		if (this.currentTransaction != null) {
			this.currentTransaction.finish();
		} else {
			throw new Exception("Cannot finish transaction, as no transaction exists!");
		}
	}



	@Override
	public void successTransaction() throws Exception {
		if (this.currentTransaction != null) {
			this.currentTransaction.success();
		} else {
			throw new Exception("Cannot call success on transaction, as no transaction exists!");
		}
	}
	
}
