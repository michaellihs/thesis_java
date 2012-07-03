package de.mknoll.thesis.analysis;

import java.io.FileWriter;
import java.util.ArrayList;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.DendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements an analyzer that plots a diagram
 * showing cluster sizes at each step when building a
 * dendrogram
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class ClusterSizeAtStepAnalyzer {

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
	public ClusterSizeAtStepAnalyzer(LoggerInterface logger, FileManager fileManager) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	/**
	 * Plots cluster-size-at-step diagram for a given dendrogram builder
	 * 
	 * @param dBuilder
	 */
	public void plotClusterSizeAtStep(DendrogramBuilder<RecommenderObject> dBuilder) {
		try {
			String plotOutputFile = this.fileManager.getResultFilePath("clusterSizeAtStep.pdf");
			this.plotClusterSizeAtStep(dBuilder, plotOutputFile);
		} catch (Exception e) {
			this.logger.log("Error while trying to create pdf output file path: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Plots cluster-size-at-step diagram for a given dendrogram builder and given output file
	 * 
	 * @param dBuilder
	 * @param outputFilePath Path to put diagram PDF file
	 */
	public void plotClusterSizeAtStep(DendrogramBuilder<RecommenderObject> dBuilder, String outputFilePath) {
		/**
		 * What do we have to do here
		 * 
		 * 1. Write step file for given dendrogram
		 * 2. Call R command that plots diagram for given steps file
		 */
		try {
			String stepClusterSizeFileName = this.fileManager.getResultFilePath("dendrogramStepClusterSize.txt");
			FileWriter fw = this.fileManager.getNewResultFileWriter("dendrogramStepClusterSize.txt");
			for (LinkDendrogram<RecommenderObject> ld : dBuilder.steps()) {
				Integer size1 = ld.dendrogram1().size();
				Integer size2 = ld.dendrogram2().size();
				if (size1 > size2) {
					fw.write(size1.toString() + "\n");
				} else {
					fw.write(size2.toString() + "\n");
				}
			}
			R r = new R();
			r.run("ClusterSizeOnMerge.r", stepClusterSizeFileName + " " + outputFilePath);
		} catch (Exception e) {
			this.logger.log("Error while trying to write cluster-sizes-per-step file: " + e.getMessage());
		}
	}
	
}
