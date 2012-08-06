package de.mknoll.thesis.tests.infohiermap;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.inspectors.RecommendationGraphConnectivityInspector;
import de.mknoll.thesis.datastructures.graph.writer.PajekWriter;
import de.mknoll.thesis.framework.data.TestResult;
import de.mknoll.thesis.framework.testsuite.AbstractTest;



/**
 * Class implements clustering experiment using Rosvall's
 * random walk clustering algorithm. For further information 
 * @see http://www.tp.umu.se/~rosvall/code.html 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class InfoHierarchicalMapTest extends AbstractTest {

	private static final String FILE_NAME = "recommendation_graph";
	
	
	
	/**
	 * Holds recommendation graph to be clustered here
	 */
	private RecommendationGraph recommendationGraph;
	
	

	@Override
	public TestResult run() throws Exception {
		this.logger.log("Start running test " + this.getClass().toString());
		
		this.init();
		
		
		// Loading recommendation graph via graph reader
		this.logger.log("Reading recommendation graph from database...");
		this.recommendationGraph = this.graphReader.read();
		this.logger.log("Size of recommendation graph: " 
				+ this.recommendationGraph.getAllRecommendations().size() + " recommendations, "
				+ this.recommendationGraph.vertexSet().size() + " nodes"
		);
		
		
		// Use this to get biggest component of graph
		RecommendationGraphConnectivityInspector connectivityInspector = new RecommendationGraphConnectivityInspector(this.recommendationGraph);
		RecommendationGraph biggestComponentSubgraph = connectivityInspector.biggestComponentAsSubgraph();
		
		
		// Write pajek format of biggest component
		PajekWriter pajekWriter = new PajekWriter();
		pajekWriter.write(biggestComponentSubgraph, this.fileManager.getResultFilePath(FILE_NAME + ".net"));
		
		
		return null;
	}

}
