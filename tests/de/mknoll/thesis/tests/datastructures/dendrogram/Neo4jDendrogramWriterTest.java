package de.mknoll.thesis.tests.datastructures.dendrogram;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramWriter;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.neo4j.Neo4jFileWriter;



/**
 * Class implements a testcase for neo4j writer
 * 
 * TODO if relevant find out how to use dummy n4j database for testing
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramWriter
 */
public class Neo4jDendrogramWriterTest {

	private RecommendationGraph recommendationGraph;
	private LinkDendrogram<RecommenderObject> dendrogram;
	private String n4jUri = "http://localhost:7474/db/data/";
	private String n4jpath = "/tmp/n4j_test/";
	private de.mknoll.thesis.neo4j.Neo4jFileWriter fileWriter;



	@Test
	public void testWrite() throws Exception {
		// TODO find out how to use in-memory testing database
		LoggerInterface logger = new ConsoleLogger();
		this.fileWriter = new Neo4jFileWriter(this.n4jpath);
		
		this.createDendrogram();
		
		Neo4jDendrogramWriter<RecommenderObject> writer = new Neo4jDendrogramWriter<RecommenderObject>(
				logger, 
				fileWriter, 
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
		Neo4jWriter graphWriter = new Neo4jWriter(this.fileWriter);
		graphWriter.write(this.recommendationGraph);
		
		
		
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
