package de.mknoll.thesis.analysis;

import java.io.FileWriter;
import java.util.ArrayList;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.DendrogramBuilder;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
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
public class NodeStepCluserSizeAnalyzer {

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
	public NodeStepCluserSizeAnalyzer(LoggerInterface logger, FileManager fileManager) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	/**
	 * Plots cluster-size-at-step diagram for a given dendrogram builder
	 * 
	 * @param dBuilder
	 */
	public void plotNodeStepClusterSize(DendrogramBuilder<RecommenderObject> dBuilder, Dendrogram<RecommenderObject> dendrogram) {
		try {
			String plotOutputFile = this.fileManager.getPlotsFilePath("nodeStepClusterSize.pdf");
			this.plotNodeStepClusterSize(dBuilder, dendrogram, plotOutputFile);
		} catch (Exception e) {
			this.logger.log("Error while trying to create pdf output file path: " + e.getMessage());
		}
	}
	
	
	
	private void plotNodeStepClusterSize(
			DendrogramBuilder<RecommenderObject> dBuilder,
			Dendrogram<RecommenderObject> dendrogram, String plotOutputFile) {
		/**
		 * What do we have to do here
		 * 
		 * 1. Write file with
		 * 
		 * 		clusterSizeAtStep1  \t  clusterSizeAtStep2  \t  clusterSizeAtStep4  \t  clusterSizeAtStep8
		 * 		clusterSizeAtStep1  \t  clusterSizeAtStep2  \t  clusterSizeAtStep4  \t  clusterSizeAtStep8
		 * 		clusterSizeAtStep1  \t  clusterSizeAtStep2  \t  clusterSizeAtStep4  \t  clusterSizeAtStep8
		 * 
		 * 2. Call R command that plots diagram for given steps file
		 */
		String nodeStepClusterSizeFileName = "";
		try {
			nodeStepClusterSizeFileName = this.fileManager.getTempFilePath("nodeStepClusterSize.txt");
			FileWriter fw = this.fileManager.getNewTempFileWriter("nodeStepClusterSize.txt");
			Integer step1 = 0, step2 = 0, step4 = 0, step8 = 0; 
			for (LeafDendrogram<RecommenderObject> ld : dendrogram.leaves()) {
				if (ld.depth() > 0) {
					step1 = ld.parent().size();
				}
				if (ld.depth() > 1) {
					step2 = ld.parent().parent().size();
				} else {
					step2 = step1;
				}
				if (ld.depth() > 3) {
					step4 = ld.parent().parent().parent().parent().size();
				} else {
					step4 = step2;
				}
				if (ld.depth() > 7) {
					step8 = ld.parent().parent().parent().parent().parent().parent().parent().parent().size();
				} else {
					step8 = step4;
				}
				fw.write(step1 + "\t" + step2 + "\t" + step4 + "\t" + step8 + "\n");
			}
			fw.close();
			R r = new R(this.logger);
			r.run("NodeStepClusterSize.r", nodeStepClusterSizeFileName + " " + plotOutputFile);
		} catch(Exception e) {
			this.logger.log("Error while trying to write node-step-cluster-size file: " + nodeStepClusterSizeFileName + " error: "  + e.getMessage());
		}
	}

}
