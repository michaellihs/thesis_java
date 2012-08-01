package de.mknoll.thesis.analysis;

import java.io.FileWriter;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudComparator;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements an analyzer for tag clouds within dendrograms.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class DendrogramTagCloudAnalyzer {
	
	private FileManager fileManager;
	
	
	
	private LoggerInterface logger;



	private Dendrogram<RecommenderObject> dendrogram;



	private LeafDendrogram<RecommenderObject>[] leaves;
	
	
	
	public DendrogramTagCloudAnalyzer(FileManager fileManager, LoggerInterface logger) {
		this.fileManager = fileManager;
		this.logger = logger;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void analyzeTagCloudsByLeavesFor(Dendrogram<RecommenderObject> dendrogram, int[] leaves) throws Exception {
		this.dendrogram = dendrogram;
		this.leaves = (LeafDendrogram<RecommenderObject>[])this.dendrogram.leaves().toArray(new LeafDendrogram[this.dendrogram.leaves().size()]);
		for (int i = 0; i < leaves.length; i++) {
			this.analyzeTagCloudsForGivenLeaf(leaves[i]);
		}
	}



	private void analyzeTagCloudsForGivenLeaf(int i) throws Exception {
		this.plotCosinSimilarity(i);
		this.plotSetDifference(i);
		this.plotNormalizedSetDifference(i);
		//this.plotNormalizedTopNSetDifference(i);
	}



	private void plotNormalizedTopNSetDifference(int i) throws Exception {
		// TODO restrict n in comparator to be <= |tags|
		this.logger.log("Start writing normalized top-n set-difference for leave " + i);
		NormalizedSetDifferenceTopNTagComparatorStrategy comparatorStrategy = new NormalizedSetDifferenceTopNTagComparatorStrategy(4);
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "normalizedTopNSetDifference_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		int maxDepth = leaf.depth();
		while(parent != null) {
			this.logger.log("Analyzing node at depth " + depth + " of " + maxDepth);
			Double similarity = comparator.compare(parent.dendrogram1().tagCloud(), parent.dendrogram2().tagCloud());
			fw.write(similarity.toString() + "\n");
			parent = parent.parent();
			depth++;
		}
		fw.close();
		R r = new R(this.logger);
		// We set x-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedTopNSetDifference_" + i + ".pdf " + "Normalized top-n set-difference")
		);
	}



	private void plotNormalizedSetDifference(int i) throws Exception {
		NormalizedSetDifferenceTagComparatorStrategy comparatorStrategy = new NormalizedSetDifferenceTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "normalizedSetDifference_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		FileWriter tagCloudsFw = this.fileManager.getNewResultFileWriter("tagCloudsWithSimilarity0_leaf" + i + ".txt");
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		while(parent != null) {
			Double similarity = comparator.compare(parent.dendrogram1().tagCloud(), parent.dendrogram2().tagCloud()); 
			fw.write(similarity.toString() + "\n");
			if ((1.0 - similarity) < 0.0001) {
				tagCloudsFw.write("Equivalent tag clouds at depth " + depth + "\n");
				tagCloudsFw.write(parent.dendrogram1().tagCloud().toString() + "\n\n");
				tagCloudsFw.write(parent.dendrogram2().tagCloud().toString() + "\n\n\n");
			}
			if ((1.0 - similarity) > 0.9999) {
				tagCloudsFw.write("Disjunct tag clouds at depth " + depth + "\n");
				tagCloudsFw.write(parent.dendrogram1().tagCloud().toString() + "\n\n");
				tagCloudsFw.write(parent.dendrogram2().tagCloud().toString() + "\n\n\n");
			}
			parent = parent.parent();
			depth++;
		}
		fw.close();
		tagCloudsFw.close();
		R r = new R(this.logger);
		// We set x-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedSetDifference_" + i + ".pdf " + "Normalized set-difference")
		);
	}



	private void plotSetDifference(int i) {
		// TODO Auto-generated method stub
		
	}



	private void plotCosinSimilarity(int i) {
		
	}
	

}
