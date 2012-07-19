package de.mknoll.thesis.tests.analysis;

import java.io.FileWriter;

import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.analysis.ComponentSizeComponentCountAnalyzer;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.inspectors.RecommendationGraphConnectivityInspector;
import de.mknoll.thesis.datastructures.graph.writer.EdgeListWriter;
import de.mknoll.thesis.framework.data.TestResult;
import de.mknoll.thesis.framework.testsuite.AbstractTest;



/**
 * Class implements a graph analyzer for gathering relevant information on graphs
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class GraphAnalyzer extends AbstractTest{

	/**
	 * Holds an instance of a recommendation graph
	 */
	private RecommendationGraph recommendationGraph;
	
	
	
	/**
	 * Holds biggest component of recommendation graph as a subgraph
	 */
	private RecommendationGraph biggestComponentSubgraph;
	
	
	
	/**
	 * Holds components count of recommendation graph
	 */
	private int componentsCount;



	private EdgeListWriter edgeListWriter;

	
	
	@Override
	public TestResult run() throws Exception {
		this.logger.log("Start running test " + this.getClass().toString());
		this.init();
		
		this.readRecommendationGraph();
		this.componentizeGraph();
		
		/**
		 *  Which analysis should be made:
		 *  
		 *  1. #Vertices #Edges
		 *  2. #Components
		 *  3. |biggest component|
		 *  4. mean degree
		 *  5. density
		 *  6. degree distribution for whole graph / biggest component
		 *  7. histogram of component sizes
		 */
		
		this.writeGraphStatistics();
		this.plotGraphDiagrams();
		
		
		this.recommendationGraph = null;
		this.biggestComponentSubgraph = null;
		this.graphReader = null;
		return null;
	}
	
	
	
	private void plotGraphDiagrams() throws Exception {
		// Writing recommendation graph to an edge list file required by Newman algorithm implementation
		this.logger.log("Writing edgelist...");
		String edgeListFilePath = this.fileManager.getResultFilePath("graph.pairs");
		this.edgeListWriter.write(this.recommendationGraph, edgeListFilePath);
		
		
		// Plot component size / component count using R
		ComponentSizeComponentCountAnalyzer cscaAnalyzer = this.container.getComponent(ComponentSizeComponentCountAnalyzer.class);
		cscaAnalyzer.plotComponentSizeComponentCount(edgeListFilePath);
	}



	private void writeGraphStatistics() throws Exception {
		FileWriter fw = this.fileManager.getNewResultFileWriter("graphAnalysis.txt");
		
		// Analyzing overall graph
		Double vertexCount = new Double(this.recommendationGraph.vertexSet().size());
		Double edgeCount = new Double(this.recommendationGraph.edgeSet().size());
		Double meanDegree = (2 * edgeCount) / vertexCount;
		Double density = meanDegree / (vertexCount - 1); // As defined in "Networks - An Introduction" p. 134
		
		fw.write("Overall graph:\n");
		fw.write("==============\n\n");
		
		fw.write("|V| = " + vertexCount + "\n");
		fw.write("|E| = " + edgeCount + "\n");
		
		fw.write("|components| = " + this.componentsCount + "\n");
		
		fw.write("mean degree = " + meanDegree + "\n");
		fw.write("density = " + density + "\n");
		fw.write("\n\n");

		
		
		// Analyzing biggest component
		Double bcVertexCount = new Double(this.biggestComponentSubgraph.vertexSet().size());
		Double bcEdgeCount = new Double(this.biggestComponentSubgraph.edgeSet().size());
		Double bcMeanDegree = (2 * bcEdgeCount) / bcVertexCount;
		Double bcDensity = bcMeanDegree / (bcVertexCount - 1);
		
		fw.write("Biggest component:\n");
		fw.write("==================\n\n");
		
		fw.write("|V| = " + bcVertexCount + "\n");
		fw.write("|E| = " + bcEdgeCount + "\n");
		fw.write("mean degree = " + bcMeanDegree + "\n");
		fw.write("density = " + bcDensity + "\n");
		
		fw.close();
	}

	
	
	/**
	 * Reads recommendation graph from database
	 * 
	 * TODO think about how to cache this
	 * 
	 * @throws Exception
	 */
	private void readRecommendationGraph() throws Exception {
		// Loading recommendation graph via graph reader
		this.logger.log("Reading recommendation graph from database...");
		this.recommendationGraph = this.graphReader.read();
		this.logger.log("Size of recommendation graph: " 
				+ this.recommendationGraph.getAllRecommendations().size() + " recommendations, "
				+ this.recommendationGraph.vertexSet().size() + " nodes"
		);
	}
	
	
	
	/**
	 * Do some components analysis on the graph
	 * 
	 * @throws Exception
	 */
	private void componentizeGraph() throws Exception {
		// Use this to get biggest component of graph
		RecommendationGraphConnectivityInspector connectivityInspector = new RecommendationGraphConnectivityInspector(this.recommendationGraph);
		this.biggestComponentSubgraph = connectivityInspector.biggestComponentAsSubgraph();
		this.componentsCount = connectivityInspector.connectedSets().size();
		connectivityInspector = null;
	}
	
	
	
	protected void init() {
		super.init();
		this.container.addComponent(
				EdgeListWriter.class, 
				EdgeListWriter.class, 
				new ConstantParameter(this.logger)
		);
		
		this.edgeListWriter = this.container.getComponent(EdgeListWriter.class);
		
		this.container.addComponent(ComponentSizeComponentCountAnalyzer.class, ComponentSizeComponentCountAnalyzer.class);
	}
	
}
