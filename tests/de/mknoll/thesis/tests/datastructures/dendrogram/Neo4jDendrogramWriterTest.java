package de.mknoll.thesis.tests.datastructures.dendrogram;

import org.junit.Test;
import org.neo4j.rest.graphdb.RestAPI;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramDbWriter;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jDbWriter;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a testcase for neo4j writer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramDbWriter
 */
public class Neo4jDendrogramWriterTest {

	private RecommendationGraph recommendationGraph;
	private LinkDendrogram<RecommenderObject> dendrogram;
	private String n4jUri = "http://localhost:7474/db/data/";



	@Test
	public void testWrite() throws Exception {
		// TODO find out how to use in-memory testing database
		LoggerInterface logger = new ConsoleLogger();
		RestAPI restApi = new RestAPI(this.n4jUri);
		
		this.createDendrogram();
		
		Neo4jDendrogramDbWriter<RecommenderObject> writer = new Neo4jDendrogramDbWriter<RecommenderObject>(
				logger, 
				restApi, 
				this.recommendationGraph.getIdNodeMap()
		);
		
		
		writer.write(this.dendrogram);
	}

	
	
	private void createDendrogram() throws Exception {
		// First we have to build up a graph containing recommendations of dendrogram
		IdNodeMap idNodeMap = new DefaultIdNodeMap();
		this.recommendationGraph = new RecommendationGraph(idNodeMap);
		
		RecommenderObject r1 = new RecommenderObject("de:1", "Ein erster Titel", "");
		RecommenderObject r2 = new RecommenderObject("de:2", "Ein zweiter Titel", "");
		RecommenderObject r3 = new RecommenderObject("de:3", "Ein dritter Titel", "");
		RecommenderObject r4 = new RecommenderObject("de:4", "Ein vierter Titel", "");
		
		// Make sure, we have Node object for every recommender object
		this.recommendationGraph.addRecommendation(r1, r2);
		this.recommendationGraph.addRecommendation(r3, r4);
		
		// Persist recommendation graph to neo4j to have n4j ids for nodes
		Neo4jDbWriter n4jGraphWriter = new Neo4jDbWriter();
		n4jGraphWriter.write(this.recommendationGraph, this.n4jUri);
		
		
		
		// Then we can build up a dendrogram
		this.dendrogram = new LinkDendrogram<RecommenderObject>(
				new LinkDendrogram<RecommenderObject>(
						new LeafDendrogram<RecommenderObject>(r1),
						new LeafDendrogram<RecommenderObject>(r2)
				), 
				new LinkDendrogram<RecommenderObject>(
						new LeafDendrogram<RecommenderObject>(r3),
						new LeafDendrogram<RecommenderObject>(r4)
				)
		);
		
	}

}
