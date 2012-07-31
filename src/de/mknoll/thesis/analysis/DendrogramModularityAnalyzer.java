package de.mknoll.thesis.analysis;

import java.io.FileWriter;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements plotting of modularity distribution of given dendrogram
 * using R.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class DendrogramModularityAnalyzer {

	private FileManager fileManager;
	
	
	
	private LoggerInterface logger;
	
	
	
	public DendrogramModularityAnalyzer(FileManager fileManager, LoggerInterface logger) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	public void plotModularityFor(Dendrogram<RecommenderObject> dendrogram, RecommendationGraph graph) throws Exception {
		ModularityAnalyzer modularityAnalyzer = new ModularityAnalyzer(graph);
		Double[] modularities = modularityAnalyzer.modularityByStep(dendrogram);
		this.plotModularities(modularities);
	}
	
	

	private void plotModularities(Double[] modularities) {
		try {
			String modularitiesFile = this.fileManager.getTempFilePath("dendrogramModularities.txt");
			String outputPath = this.fileManager.getPlotsFilePath("dendrogramModularities.pdf");
			this.writeModularitiesToFile(modularities);
			this.plot(modularitiesFile, outputPath);
		} catch (Exception e) {
			this.logger.log("Error while trying to write dendrogram modularities to file: " + e.getMessage());
		}
	}



	private void plot(String modularitiesFile, String outputPath) {
		R r = new R(this.logger);
		r.run("DendrogramModularity.r", modularitiesFile + " " + outputPath);
	}



	private void writeModularitiesToFile(Double[] modularities) throws Exception {
		FileWriter fw = this.fileManager.getNewPlotsFileWriter("dendrogramModularities.txt");
		for (int i = 0; i < modularities.length; i++) {
			fw.write(modularities[i] + "\n");
		}
		fw.close();
	}
	
}
