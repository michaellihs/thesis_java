package de.mknoll.thesis.tests.newman;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

import org.neo4j.rest.graphdb.RestAPI;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.annotations.Inject;
import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.analysis.ClusterSizeAtStepAnalyzer;
import de.mknoll.thesis.analysis.ComponentSizeComponentCountAnalyzer;
import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.JsonDendrogramWriter;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.Neo4jDendrogramWriter;
import de.mknoll.thesis.datastructures.dendrogram.NewmanDendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.NewmanJoinsDendrogramReader;
import de.mknoll.thesis.datastructures.dendrogram.RecommenderObjectDendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.XmlDendrogramWriter;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdProvider;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.reader.GraphReader;
import de.mknoll.thesis.datastructures.graph.reader.PostgresReader;
import de.mknoll.thesis.datastructures.graph.writer.EdgeListWriter;
import de.mknoll.thesis.datastructures.graph.writer.Neo4jWriter;
import de.mknoll.thesis.externaltools.wrapper.FastModularity;
import de.mknoll.thesis.framework.data.TestResult;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.framework.testsuite.AbstractTest;
import de.mknoll.thesis.neo4j.Neo4jFileWriter;



/**
 * Class implements a test for Newman clustering. 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class NewmanTest extends AbstractTest {

	private static final String FILE_NAME = "recommendation_graph";
	
	
	
	/**
	 * Holds recommendation graph to be clustered here
	 */
	private RecommendationGraph recommendationGraph;
	
	
	
	/**
	 * Holds edgelist writer for writing graphs in edgelist format
	 */
	private EdgeListWriter edgeListWriter;
	
	
	
	/**
	 * Holds an instance of a graph db writer (either file writer or db writer)
	 * 
	 * This object is test-wide singleton keeping track of which neo4j db we want to
	 * write into. Used by graph writers and dendrogram writers to make sure, that 
	 * the same database is used whenever writing to n4j.
	 */
	private de.mknoll.thesis.neo4j.Neo4jWriter neo4jWriter;
	
	
	
	/**
	 * Actually runs the test itself.
	 * @throws Exception 
	 * 
	 * @Override
	 */
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
		
		
		// Write recommendation graph to Neo4J graph database
		/*
		this.logger.log("Writing recommendation graph to N4J database...");
		Neo4jDbWriter n4jWriter = new Neo4jDbWriter();
		n4jWriter.write(this.recommendationGraph, this.testConfiguration.getNeo4jUrl());
		*/
		
		
		// Write recommendation graph to Neo4J file
//		this.logger.log("Writing recommendation graph to N4J file...");
//		Neo4jWriter n4jWriter = new Neo4jWriter(this.neo4jWriter);
//		n4jWriter.write(this.recommendationGraph);

		
		// Writing recommendation graph to an edge list file required by Newman algorithm implementation
		this.logger.log("Writing edgelist...");
		String edgeListFilePath = this.fileManager.getResultFilePath(FILE_NAME + ".pairs");
		this.edgeListWriter = this.container.getComponent(EdgeListWriter.class);
		this.edgeListWriter.write(this.recommendationGraph, edgeListFilePath);
		
		
		// Calling Newman algorithm implementation to cluster recommendation graph
		HashMap<String, String> arguments = new HashMap<String, String>();
		arguments.put("-f", edgeListFilePath);
		FastModularity fastModularity = this.container.getComponent(FastModularity.class);
		fastModularity.logOutput(false); // Remove this line, if you want to get output of algorithm as debug output
		fastModularity.run(arguments);
		
		
		// Read cluster results (dendrogram) from Newman algorithm into datastructure
		String dendrogramFile = this.fileManager.getCurrentResultsPath() + FILE_NAME + "-fc_a.joins";
		NewmanDendrogramBuilder dBuilder = new NewmanDendrogramBuilder(this.recommendationGraph.getIdNodeMap());
		NewmanJoinsDendrogramReader dendrogramReader = new NewmanJoinsDendrogramReader(dBuilder, this.logger);
		LinkDendrogram<RecommenderObject> dendrogram = (LinkDendrogram<RecommenderObject>) dendrogramReader.read(dendrogramFile);
		this.logger.log("Size of dendrogram: " + dendrogram.memberSet().size());
		
		
		// Plot step-cluster-size
		ClusterSizeAtStepAnalyzer analyzer1 = this.container.getComponent(ClusterSizeAtStepAnalyzer.class);
		analyzer1.plotClusterSizeAtStep(dBuilder);
		
		
		// Write cluster results (dendrogram) to neo4j graph database
//		Neo4jDendrogramWriter<RecommenderObject> writer = new Neo4jDendrogramWriter<RecommenderObject>(logger, this.neo4jWriter, this.container.getComponent(IdNodeMap.class));
//		this.logger.log("Start writing dendrogram into neo4j");
//		writer.write(dendrogram);
//		this.logger.log("Finished writing dendrogram into neo4j");
		
		// Write dendrogram to JSON file
		//JsonDendrogramWriter<RecommenderObject> writer = new JsonDendrogramWriter<RecommenderObject>();
		//String jsonDendrogramString = writer.write(dendrogram);
		
		
		// Write dendrogram to XML file
		//XmlDendrogramWriter<RecommenderObject> xmlWriter = this.container.getComponent(XmlDendrogramWriter.class);
		//xmlWriter.write(dendrogram);

		
		// Write partioning with 20 partitions
		//this.logger.log("Writing k=20 partitioning into file");
		//Set<Set<RecommenderObject>> partitioning = dendrogram.partition(20);
		//this.writePartitioning(partitioning);
		
		
		// Generate paritioning of 20 dendrograms and write them to individual JSON files
		//Set<Dendrogram<RecommenderObject>> dendrograms = dendrogram.partitionDendrogram(20);
		//int dendrogramCounter = 1;
		//for(Dendrogram<RecommenderObject> d : dendrograms) {
		//	this.writeDendrogramToJson(d, dendrogramCounter++);
		//}

		
		return new TestResult();
	}


	
	@SuppressWarnings("unused")
	private void writeDendrogramToJson(Dendrogram<RecommenderObject> d, int i) {
		String fileName = "dendrogram_" + i + "_" + d.size() + ".js";
		JsonDendrogramWriter<RecommenderObject> jsonWriter = new JsonDendrogramWriter<RecommenderObject>();
		String jsonString = jsonWriter.write(d);
		try {
			this.logger.log("Writing Dendrogram into file " + this.fileManager.getCurrentResultsPath() + fileName);
			FileWriter fileWriter = this.fileManager.getNewResultFileWriter("dendrogram_" + i + "_" + d.size() + ".js");
			fileWriter.write("var flare = " + jsonString + ";");
			fileWriter.close();
		} catch (Exception e) {
			this.logger.log("Error while trying to write dendrogram to json file: " + e.getMessage());
		}
	}



	@SuppressWarnings("unused")
	private void writePartitioning(Set<Set<RecommenderObject>> partitioning) {
		String filePath = "No path generated!";
		try {
			FileWriter fileWriter = this.fileManager.getNewResultFileWriter("k-20_partitoining.txt");
			filePath = this.fileManager.getResultFilePath("k-20_partitoining.txt");
			for (Set<RecommenderObject> partition : partitioning) {
				fileWriter.write("Partition (" + partition.size() + ") \n " + partition.toString() + "\n\n");
				fileWriter.close();
			}
		} catch (Exception e) {
			this.logger.log("Error when trying to write partioning to file: " + filePath);
		}
		
	}



	/**
	 * Initializes this test
	 */
	protected void init() {
		super.init();
				
		this.container.addComponent(
				EdgeListWriter.class, 
				EdgeListWriter.class, 
				new ConstantParameter(this.logger)
		);
		
		this.container.addComponent(
				FastModularity.class,
				FastModularity.class,
				new ConstantParameter(new String("/Users/mimi/Dropbox/Diplomarbeit/Code/Newman/FastCommunity_GPL_v1.0.3")),
				new ConstantParameter(this.container.getComponent(LoggerInterface.class))
		);
		
		this.container.addComponent(NewmanJoinsDendrogramReader.class);
		this.container.addComponent(XmlDendrogramWriter.class);
		
		this.container.addComponent(ClusterSizeAtStepAnalyzer.class, ClusterSizeAtStepAnalyzer.class);
		this.container.addComponent(ComponentSizeComponentCountAnalyzer.class, ComponentSizeComponentCountAnalyzer.class);
		
		this.neo4jWriter = new Neo4jFileWriter(this.fileManager.getCurrentNeo4jPath());
		
	}
	
	
	
	public void shutdown() {
		this.neo4jWriter.shutdown();
	}

}
