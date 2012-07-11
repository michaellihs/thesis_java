package de.mknoll.thesis.tests.datastructures.graph.writer;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter;
import de.mknoll.thesis.tests.div.MockGraphBuilder;



/**
 * Class implements a testcase for neo4j graph writer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.writer.Neo4jDbWriter
 */
public class Neo4jWriterTest {

	@Test
	public void testWrite() throws Exception {
		RecommendationGraph recGraph = MockGraphBuilder.createRecommendationTestGraph();
		de.mknoll.thesis.neo4j.Neo4jFileWriter fileWriter = new de.mknoll.thesis.neo4j.Neo4jFileWriter("/tmp/neo4j-tests/");
		Neo4jWriter writer = new Neo4jWriter(fileWriter);
		writer.write(recGraph);
	}

}
