package de.mknoll.thesis.analysis;

import java.io.FileWriter;
import java.util.ArrayList;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.DendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.EdgeListWriter;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements an analyzer that plots a diagram
 * showing component size and component count for given
 * graph
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class ComponentSizeComponentCountAnalyzer {

	/**
	 * Holds file manager used to generate some temp files and output file for diagram
	 */
	FileManager fileManager;
	
	
	
	/**
	 * Logger for generating some progress information
	 */
	LoggerInterface logger;
	
	
	
	/**
	 * Constructor taking dependencies as parameters
	 * 
	 * @param logger
	 * @param fileManager
	 */
	public ComponentSizeComponentCountAnalyzer(LoggerInterface logger, FileManager fileManager) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	/**
	 * Plots component-size-component-count diagram for a given recommendation graph
	 * 
	 * @param graph
	 */
	public void plotComponentSizeComponentCount(RecommendationGraph graph) {
		try {
			String plotOutputPath = this.fileManager.getCurrentPlotsPath();
			this.plotComponentSizeComponentCount(graph, plotOutputPath);
		} catch (Exception e) {
			this.logger.log("Error while trying to create pdf output file path: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Plots component-size-component-count diagram for a given recommendation graph and given output file
	 * 
	 * @param dBuilder
	 * @param outputFilePath Path to put diagram PDF file
	 */
	public void plotComponentSizeComponentCount(RecommendationGraph graph, String outputPath) {
		try {
			String graphEdgeListFilePath = this.fileManager.getTempFilePath("recGraphEdgeList.txt");
			EdgeListWriter writer = new EdgeListWriter(this.logger);
			writer.write(graph, graphEdgeListFilePath);
			this.plot(graphEdgeListFilePath, outputPath);
		} catch (Exception e) {
			this.logger.log("Error while trying to write edgelist to file: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Plots component-size-component-count diagram for a given input file path
	 * 
	 * @param dBuilder
	 * @param outputFilePath Path to put diagram PDF file
	 */
	public void plotComponentSizeComponentCount(String inputFilePath) {
		try {
			String outputFilePath = this.fileManager.getCurrentPlotsPath();
			this.plotComponentSizeComponentCount(inputFilePath, outputFilePath);
		} catch (Exception e) {
			this.logger.log("Error while trying to plot diagram: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Plots component-size-component-count diagram for given input- and outputfile
	 * 
	 * @param outputFilePath
	 * @param inputFilePath
	 */
	public void plotComponentSizeComponentCount(String inputFilePath, String outputPath) {
		this.plot(inputFilePath, outputPath);
	}
	
	
	
	private void plot(String inputFilePath, String outputPath) {
		R r = new R(this.logger);
		r.run("ComponentSizeComponentCount.r", inputFilePath + " " + outputPath);
	}
	
}
