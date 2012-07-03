package de.mknoll.thesis.tests.datastructures.graph.writer;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter;
import de.mknoll.thesis.tests.div.MockGraphBuilder;



/**
 * Class implements a testcase for neo4j graph writer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter
 */
public class Neo4jWriterTest {

	@Test
	public void testWrite() throws Exception {
		RecommendationGraph recGraph = MockGraphBuilder.createRecommendationTestGraph();
		Neo4jWriter writer = new Neo4jWriter();
		writer.write(recGraph, "http://localhost:7474/db/data/");
	}

}
