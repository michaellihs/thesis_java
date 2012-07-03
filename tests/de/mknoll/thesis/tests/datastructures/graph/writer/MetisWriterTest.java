package de.mknoll.thesis.tests.datastructures.graph.writer;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.writer.MetisWriter;
import de.mknoll.thesis.tests.div.MockGraphBuilder;



/**
 * Class implements testcase for Metis graph writer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.writer.MetisWriter
 */
public class MetisWriterTest {

	@Test
	public void testWrite() throws Exception {
		RecommendationGraph graphMock = MockGraphBuilder.createRecommendationTestGraph();
		MetisWriter writer = new MetisWriter();
		writer.write(graphMock, "/tmp/metisTest.graph");
	}

}
