package de.mknoll.thesis.datastructures.graph.writer;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Class implements graph writer for writing a graph into
 * a Neo4j file via Neo4j API
 * 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Neo4jFileWriter extends Neo4jWriter {
	
	/**
	 * Holds properties path for neo4j database
	 */
	private String pathToConfig = "/Applications/Utilities/neo4j-advanced-1.8-SNAPSHOT/conf/";
	
	
	
	private String destination;
	
	
	
	private GraphDatabaseService graphDb;
	
	
	
	private de.mknoll.thesis.neo4j.Neo4jFileWriter writer;
	
	

	@Override
	public void write(RecommendationGraph graph, String destination) throws Exception {
		this.destination = destination;
		this.recommendationGraph = graph;
		this.init();
		this.writeGraphToDestination(graph);
	}
	
	
	
	private void writeGraphToDestination(RecommendationGraph graph) throws Exception {
		Transaction tx = graphDb.beginTx();
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
			
			tx.success();
			
		} finally {
			tx.finish();
		}
	}



	private void init() {
		this.graphDb = new GraphDatabaseFactory() .
    		newEmbeddedDatabaseBuilder(this.destination) .
    		loadPropertiesFromFile(this.pathToConfig + "neo4j.properties") .
    		newGraphDatabase();
		
		this.writer = new de.mknoll.thesis.neo4j.Neo4jFileWriter(this.graphDb);
	}
	
	
	
	/**
	 * Sets path to neo4j config directory 
	 * 
	 * @param pathToConfig
	 */
	public void setPathToConfig(String pathToConfig) {
		this.pathToConfig = pathToConfig;
	}



	@Override
	protected Node createNode(Map<String, Object> properties) {
		return this.writer.createNode(properties);
	}



	@Override
	protected Relationship createRelationship(Node sourceNode, Node targetNode,
			RecommenderRelationshipTypes type, Map<String, Object> properties) {
		return this.writer.createRelationship(sourceNode, targetNode, type, properties);
	}

}
