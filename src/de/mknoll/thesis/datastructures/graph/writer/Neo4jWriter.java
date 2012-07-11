package de.mknoll.thesis.datastructures.graph.writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.neo4j.Neo4jDbWriter;
import de.mknoll.thesis.neo4j.Neo4jFileWriter;



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
public class Neo4jWriter {
	
	/**
	 * Returns a new instance of this class for writing into 
	 * database server given by URL
	 * 
	 * @param databaseUrl URL of database to be used for writing
	 * @return Neo4j writer for given database URL
	 */
	public static Neo4jWriter getDbWriterForDatabaseUrl(String databaseUrl) {
		de.mknoll.thesis.neo4j.Neo4jDbWriter dbWriter = new Neo4jDbWriter(databaseUrl);
		return new Neo4jWriter(dbWriter);
	}
	
	
	
	/**
	 * Returns a new instance of this class for writing into database
	 * files given by database path
	 * 
	 * @param databasePath Path to database files used as storage
	 * @return Neo4j writer for given database path
	 */
	public static Neo4jWriter getFileWriterForDatabasePath(String databasePath) {
		de.mknoll.thesis.neo4j.Neo4jFileWriter fileWriter = new Neo4jFileWriter(databasePath);
		return new Neo4jWriter(fileWriter);
	}
	
	
	

	/**
	 * Holds mapping of already inserted nodes
	 */
	protected Map<String, Node> neo4jIdToNodeMapping = new HashMap<String, Node>();



	/**
	 * Holds recommendation graph currently written to database
	 */
	protected RecommendationGraph recommendationGraph;
	
	
	
	/**
	 * Holds graph writer for neo4j storage
	 */
	private de.mknoll.thesis.neo4j.Neo4jWriter writer;
	
	
	
	/**
	 * Constructor takes writer class for concrete graph storage
	 * 
	 * @param writer
	 */
	public Neo4jWriter(de.mknoll.thesis.neo4j.Neo4jWriter writer) {
		this.writer = writer;
	}
	
	
	
	/**
	 * Writes given graph into Neo4j database.
	 * 
	 * @throws Exception 
	 * @Override
	 */
	public void write(RecommendationGraph graph) throws Exception {
		this.recommendationGraph = graph;
		
		// Try to write graph in a single transaction
		this.writer.beginTransaction();
		try {
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
			this.writer.successTransaction();
		} finally {
			this.writer.finishTransaction();
		}
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
	protected Node createNode(Map<String, Object> properties) {
		return this.writer.createNode(properties);
	}
	
	
	
	/**
	 * Creates a relationship for given source node and given target node and given properties
	 * 
	 * @param sourceNode
	 * @param targetNode
	 * @param type
	 * @param properties
	 * @return
	 */
	protected Relationship createRelationship(Node sourceNode, Node targetNode,
			 RecommenderRelationshipTypes type, Map<String,Object> properties) {
		return this.writer.createRelationship(sourceNode, targetNode, type, properties);
	}

}