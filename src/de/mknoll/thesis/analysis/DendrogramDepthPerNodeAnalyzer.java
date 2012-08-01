package de.mknoll.thesis.analysis;

import java.io.FileWriter;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements an analyzer for plotting dendrogram depth for each
 * leaf node in dendrogram.
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class DendrogramDepthPerNodeAnalyzer {

	private FileManager fileManager;
	
	
	
	private LoggerInterface logger;
	
	
	
	public DendrogramDepthPerNodeAnalyzer(FileManager fileManager, LoggerInterface logger) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	public void plotPerLeafDepthFor(Dendrogram<RecommenderObject> dendrogram) throws Exception {
		String tmpFilePath = this.fileManager.getTempFilePath("dendrogramDepthPerNode.txt");
		FileWriter fw = this.fileManager.getNewTempFileWriter("dendrogramDepthPerNode.txt");
		for (LeafDendrogram<RecommenderObject> leaf : dendrogram.leaves()) {
			fw.write(leaf.depth() + "\n");
		}
		fw.close();
		R r = new R(this.logger);
		String outputFilePath = this.fileManager.getPlotsFilePath("dendrogramDepthPerNode.pdf");
		r.run("DendrogramDepthPerNode.r", tmpFilePath + " " + outputFilePath);
	}
	
}
