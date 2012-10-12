package de.mknoll.thesis.tests.rgmc;

import java.util.HashMap;

import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.analysis.ClusterSizeAtStepAnalyzer;
import de.mknoll.thesis.analysis.DendrogramDepthPerNodeAnalyzer;
import de.mknoll.thesis.analysis.DendrogramModularityAnalyzer;
import de.mknoll.thesis.analysis.DendrogramTagCloudAnalyzer;
import de.mknoll.thesis.analysis.NodeStepCluserSizeAnalyzer;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramWriter;
import de.mknoll.thesis.datastructures.dendrogram.NewmanJoinsDendrogramReader;
import de.mknoll.thesis.datastructures.dendrogram.RecommenderObjectDendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.RgmcDendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.XmlDendrogramWriter;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.inspectors.RecommendationGraphConnectivityInspector;
import de.mknoll.thesis.datastructures.graph.writer.MetisWriter;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter;
import de.mknoll.thesis.externaltools.wrapper.RandomizedGreedyModularityClustering;
import de.mknoll.thesis.framework.data.TestResult;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.framework.testsuite.AbstractTest;
import de.mknoll.thesis.neo4j.Neo4jFileWriter;



/**
 * Class implements clustering experiment with
 * RandomizedGreedyModularity clustering.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class RandomizedGreedyModularityTest extends AbstractTest {
	
	private static final String FILE_NAME = "recommendation_graph";
	
	
	
	/**
	 * Holds recommendation graph to be clustered here
	 */
	private RecommendationGraph recommendationGraph;
	
	
	
	/**
	 * Holds Metis writer for writing graph in Metis format
	 */
	private MetisWriter metisWriter;



	/**
	 * Holds neo4j writer to write stuff into node database
	 */
	private Neo4jFileWriter neo4jWriter;



	/**
	 * If set to true, graph will be written to neo4j database
	 */
	private Boolean writeRecommendationGraphToNeo4j;



	/**
	 * If set to true, dendrogram will be written to neo4j database
	 */
	private Boolean writeDendrogramToNeo4j;



	/**
	 * If set to true, diagram with cluster size at each node with given distance from leaf will be plotted
	 */
	private boolean plotNodeStepClusterSize;



	/**
	 * If set to true, diagram of cluster sizes along the inclusion path will be plotted
	 */
	private boolean plotClusterSizeAtStep;



	/**
	 * If set to true, modularity at each cut in dendrogram will be calculated (warning: takes a lot of time!)
	 */
	private boolean plotModularitiesForDendrogram;



	/**
	 * If set to true, the length of inclusion path for each leaf will be calculated
	 */
	private boolean plotDendrogramDepthPerNode;



	/**
	 * If set to true, some information on tag clouds will be calculated
	 */
	private boolean plotTagCloudAnalysisForDendrogram;

	
	
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
		
		
		// Write recommendation graph to Neo4J graph database
		//this.logger.log("Writing recommendation graph to N4J database...");
		//Neo4jWriter n4jWriter = new Neo4jWriter();
		//n4jWriter.write(this.recommendationGraph, "http://localhost:7474/db/data/");
		
		
		// Write recommendation graph to Neo4J file
		if (this.writeRecommendationGraphToNeo4j) {
			this.logger.log("Writing recommendation graph to N4J file...");
			Neo4jWriter n4jWriter = new Neo4jWriter(this.neo4jWriter);
			n4jWriter.write(this.recommendationGraph);
		}
		
		
		// Writing recommendation graph to an Metis file required by Randomized Greedy Modularity Clustering
		this.logger.log("Writing METIS file...");
		String metisFilePath = this.fileManager.getResultFilePath(FILE_NAME + ".graph");
		this.metisWriter = this.container.getComponent(MetisWriter.class);
		this.metisWriter.write(biggestComponentSubgraph, metisFilePath);
		//this.metisWriter.write(this.recommendationGraph, metisFilePath);
		
		
		// Calling RGMC algorithm implementation to cluster recommendation graph
		this.logger.log("Start clustering via randomized greedy modularity clustering...");
		HashMap<String, String> arguments = new HashMap<String, String>();
		arguments.put("--file", metisFilePath);
		arguments.put("--joinsfile", this.fileManager.getCurrentResultsPath() + FILE_NAME + ".joins");
		RandomizedGreedyModularityClustering rgmc = this.container.getComponent(RandomizedGreedyModularityClustering.class);
		rgmc.logOutput(false); // Remove this line, if you want to get output of algorithm as debug output
		rgmc.run(arguments);
		
		
		// Read cluster results (dendrogram) from RGMC algorithm into datastructure
		this.logger.log("Start reading dendrogram from joins file...");
		String dendrogramFile = this.fileManager.getCurrentResultsPath() + FILE_NAME + ".joins"; 
		RgmcDendrogramBuilder dBuilder = new RgmcDendrogramBuilder(biggestComponentSubgraph.getIdNodeMap());
		NewmanJoinsDendrogramReader dendrogramReader = new NewmanJoinsDendrogramReader(dBuilder, this.logger);
		LinkDendrogram<RecommenderObject> dendrogram = (LinkDendrogram<RecommenderObject>) dendrogramReader.read(dendrogramFile);
		this.logger.log("Size of dendrogram: " + dendrogram.memberSet().size());
		
		
		// Plot step-cluster-size
		if (this.plotClusterSizeAtStep) {
			ClusterSizeAtStepAnalyzer analyzer1 = this.container.getComponent(ClusterSizeAtStepAnalyzer.class);
			analyzer1.plotClusterSizeAtStep(dBuilder);
		}
		
		
		// Plot node-step-cluster size (for each node plots cluster sizes at step 1,2,4,8)
		if (this.plotNodeStepClusterSize) {
			NodeStepCluserSizeAnalyzer nscsAnalyzer = this.container.getComponent(NodeStepCluserSizeAnalyzer.class);
			nscsAnalyzer.plotNodeStepClusterSize(dBuilder, dendrogram);
		}
		
		
		// Write cluster results (dendrogram) to neo4j graph database
		/*
		String uri = "http://localhost:7474/db/data/";
		LoggerInterface logger = new ConsoleLogger();
		RestAPI restApi = new RestAPI(uri);
		Neo4jDendrogramWriter<RecommenderObject> writer = new Neo4jDendrogramWriter<RecommenderObject>(logger, restApi, this.container.getComponent(IdNodeMap.class));
		this.logger.log("Start writing dendrogram into neo4j");
		writer.write(dendrogram);
		this.logger.log("Finished writing dendrogram into neo4j");
		
		this.logger.log("Finished running test " + this.getClass().toString());
		*/
		
		
		// Write cluster results (dendrogram) to neo4j graph database
		if (this.writeDendrogramToNeo4j) {
			Neo4jDendrogramWriter<RecommenderObject> writer = new Neo4jDendrogramWriter<RecommenderObject>(logger, this.neo4jWriter, this.container.getComponent(IdNodeMap.class));
			this.logger.log("Start writing dendrogram into neo4j");
			writer.write(dendrogram);
			this.logger.log("Finished writing dendrogram into neo4j");
		}
		
		
		// Plot modularities for dendrogram
		if (this.plotModularitiesForDendrogram) {
			this.logger.log("Start plotting modularities in dendrogram...");
			DendrogramModularityAnalyzer dendrogramModularityAnalyzer = this.container.getComponent(DendrogramModularityAnalyzer.class);
			dendrogramModularityAnalyzer.plotModularityFor(dendrogram, biggestComponentSubgraph);
		}
		
		
		// Plot dendrogram depth per node for dendrogram
		if (this.plotDendrogramDepthPerNode) {
			this.logger.log("Start plotting dendrogram depth per node...");
			DendrogramDepthPerNodeAnalyzer dendrogramDepthPerNodeAnalyzer = this.container.getComponent(DendrogramDepthPerNodeAnalyzer.class);
			dendrogramDepthPerNodeAnalyzer.plotPerLeafDepthFor(dendrogram);
		}
		
		
		// Plot tag cloud analysis for dendrogram
		if (this.plotTagCloudAnalysisForDendrogram) {
			this.logger.log("Start plotting tag cloud analysis for dendrogram...");
			DendrogramTagCloudAnalyzer dendrogramTagCloudAnalyzer = this.container.getComponent(DendrogramTagCloudAnalyzer.class);
			int[] leaves = {1,10,1000};
			dendrogramTagCloudAnalyzer.analyzeTagCloudsByLeavesFor(dendrogram, leaves);
		}
		
		
		return null;
	}
	
	
	
	protected void init() {
		super.init();
		this.initConfiguration();
		this.container.addComponent(MetisWriter.class);
		
		// Adding clustering wrapper to container
		this.container.addComponent(
				RandomizedGreedyModularityClustering.class,
				RandomizedGreedyModularityClustering.class,
				new ConstantParameter(new String("/Users/mimi/Dropbox/Diplomarbeit/Code/rgmc_release1_mod/dist/Debug/GNU-MacOSX")),
				new ConstantParameter(this.container.getComponent(LoggerInterface.class))
		);
		
		this.container.addComponent(NewmanJoinsDendrogramReader.class);
		this.container.addComponent(XmlDendrogramWriter.class);
		
		this.container.addComponent(ClusterSizeAtStepAnalyzer.class, ClusterSizeAtStepAnalyzer.class);
		this.container.addComponent(NodeStepCluserSizeAnalyzer.class, NodeStepCluserSizeAnalyzer.class);
		this.container.addComponent(DendrogramModularityAnalyzer.class, DendrogramModularityAnalyzer.class);
		this.container.addComponent(DendrogramDepthPerNodeAnalyzer.class, DendrogramDepthPerNodeAnalyzer.class);
		this.container.addComponent(DendrogramTagCloudAnalyzer.class, DendrogramTagCloudAnalyzer.class);
		
		this.neo4jWriter = new Neo4jFileWriter(this.fileManager.getCurrentNeo4jPath());
	}



	private void initConfiguration() {
		this.writeRecommendationGraphToNeo4j = (Boolean)this.testConfiguration.getYamlConfiguration().get("WriteRecommendationGraphToNeo4j");
		this.writeDendrogramToNeo4j = (Boolean)this.testConfiguration.getYamlConfiguration().get("WriteDendrogramToNeo4j");
		this.plotNodeStepClusterSize = (Boolean)this.testConfiguration.getYamlConfiguration().get("PlotNodeStepClusterSize");
		this.plotClusterSizeAtStep = (Boolean)this.testConfiguration.getYamlConfiguration().get("PlotClusterSizeAtStep");
		this.plotModularitiesForDendrogram = (Boolean)this.testConfiguration.getYamlConfiguration().get("PlotModularitiesForDendrogram");
		this.plotDendrogramDepthPerNode = (Boolean)this.testConfiguration.getYamlConfiguration().get("PlotDendrogramDepthPerNode");
		this.plotTagCloudAnalysisForDendrogram = (Boolean)this.testConfiguration.getYamlConfiguration().get("PlotTagCloudAnalysisForDendrogram");
	}

}
