package de.mknoll.thesis.analysis;

import java.io.FileWriter;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.CosineSimilarityTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.StopWordFilteredTagCloud;
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
	
	/**
	 * If set to true, we get some additional output
	 */
	private static final boolean ANALYSIS = false;
	
	
	
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
		
		// Reset tag clouds of dendrogram
		dendrogram.resetTagCloud();
		
		this.leaves = (LeafDendrogram<RecommenderObject>[])this.dendrogram.leaves().toArray(new LeafDendrogram[this.dendrogram.leaves().size()]);
		for (int i = 0; i < leaves.length; i++) {
			this.analyzeTagCloudsForGivenLeaf(leaves[i]);
		}
	}



	private void analyzeTagCloudsForGivenLeaf(int i) throws Exception {
		// TODO refactor this to prevent duplicated code
		
		// Without any tag modification
		this.plotCosinSimilarity(i);
		this.plotSetDifference(i);
		this.plotNormalizedSetDifference(i);
		// TODO think we have a problem here, as allTags() does not return frequencies of tags
		//this.plotNormalizedTopNSetDifference(i);
		
		// With extended stopwords list
		this.plotNormalizedSetDifferenceWithStopWords(i);
		this.plotCosinSimilarityWithStopWords(i);
		// TODO think we have a problem here, as allTags() does not return frequencies of tags
		//this.plotNormalizedTopNSetDifferenceWithStopWords(i);
		this.plotSetDifferenceWithStopWords(i);
		
		// Use differences along path rather then on nodes
		this.plotNormalizedSetDifferenceWithStopWordsAlongPath(i);
		
		
		// Do comparison plot
		this.plotComparison(i);
	}



	private void plotNormalizedSetDifferenceWithStopWordsAlongPath(int i) throws Exception {
		NormalizedSetDifferenceTagComparatorStrategy comparatorStrategy = new NormalizedSetDifferenceTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		Dendrogram<RecommenderObject> child = this.leaves[i];
		String fileName = "normalizedSetDifference_alongPath_stopWords_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		FileWriter tagCloudsFw = this.fileManager.getNewResultFileWriter("normalizedSetDifference_alongPath_tagCloudsWithSimilarity0_leaf" + i + ".txt");
		Dendrogram<RecommenderObject> parent = child.parent();
		int depth = 1;
		int maxDepth = child.depth();
		while(parent != null) {
			this.logger.log("Analyzing node at depth " + depth + " of " + maxDepth);
			
			// Filter stopwords from given clouds
			StopWordFilteredTagCloud swfCloud1 = new StopWordFilteredTagCloud(child.tagCloud());
			StopWordFilteredTagCloud swfCloud2 = new StopWordFilteredTagCloud(parent.tagCloud());
			
			Double similarity = comparator.compare(swfCloud1, swfCloud2);
			
			// Do some analysis
			//if (DendrogramTagCloudAnalyzer.ANALYSIS) {
				if ((1.0 - similarity) < 0.2) {
					tagCloudsFw.write("Equivalent tag clouds at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
				if ((1.0 - similarity) > 0.8) {
					tagCloudsFw.write("Disjunct tag clouds at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
			//}
			
			fw.write(similarity.toString() + "\n");
			parent = parent.parent();
			depth++;
		}
		tagCloudsFw.close();
		fw.close();
		R r = new R(this.logger);
		// We set x-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedSetDifference_alongPath_stopWords_" + i + ".pdf") + " normalized_set_difference_along_path"
		);
		
	}



	private void plotNormalizedTopNSetDifferenceWithStopWords(int i) {
		
	}



	private void plotSetDifferenceWithStopWords(int i) throws Exception {
		SetDifferenceTagComparatorStrategy comparatorStrategy = new SetDifferenceTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "setDifference_stopWords_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		while(parent != null) {
			StopWordFilteredTagCloud swfCloud1 = new StopWordFilteredTagCloud(parent.dendrogram1().tagCloud());
			StopWordFilteredTagCloud swfCloud2 = new StopWordFilteredTagCloud(parent.dendrogram2().tagCloud());
			Double similarity = comparator.compare(swfCloud1, swfCloud2); 
			fw.write(similarity.toString() + "\n");
			parent = parent.parent();
			depth++;
		}
		fw.close();
		R r = new R(this.logger);
		// We set y-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("setDifference_stopWords" + i + ".pdf") + " set_difference"
		);
	}



	private void plotComparison(int i) throws Exception {
		String tmpFilePath = this.fileManager.getCurrentTempPath();
		String normalizedSetDifferenceWithStopWordsFileName = tmpFilePath + "normalizedTopNSetDifference_stopWords_" + i + ".txt";
		String normalizedTopNSetDifferenceFileName = tmpFilePath + "normalizedTopNSetDifference_" + i + ".txt";
		String normalizedSetDifferenceFileName = tmpFilePath + "normalizedSetDifference_" + i + ".txt";
		String setDifferenceFileName = tmpFilePath + "setDifference_" + i + ".txt";
		
		String outputFile = this.fileManager.getPlotsFilePath("tagComparison_" + i + ".pdf");
		
		R r = new R(this.logger);
		r.run("TagCloudComparison.r", normalizedSetDifferenceWithStopWordsFileName + " " + normalizedTopNSetDifferenceFileName + " " + normalizedSetDifferenceFileName + " " + setDifferenceFileName + " " + outputFile);
	}



	private void plotCosinSimilarityWithStopWords(int i) throws Exception {
		this.logger.log("Start writing stop-word filtered cosine similarity for leave " + i);
		CosineSimilarityTagComparatorStrategy comparatorStrategy = new CosineSimilarityTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "cosineSimilarity_stopWords_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		FileWriter tagCloudsFw = this.fileManager.getNewResultFileWriter("cosineSimilarity_tagCloudsWithSimilarity0_leaf" + i + ".txt");
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		int maxDepth = leaf.depth();
		while(parent != null) {
			//this.logger.log("Analyzing node at depth " + depth + " of " + maxDepth);
			
			// Filter stopwords from given clouds
			StopWordFilteredTagCloud parentCloud = new StopWordFilteredTagCloud(parent.tagCloud());
			StopWordFilteredTagCloud swfCloud1 = new StopWordFilteredTagCloud(parent.dendrogram1().tagCloud());
			StopWordFilteredTagCloud swfCloud2 = new StopWordFilteredTagCloud(parent.dendrogram2().tagCloud());
			
			Double similarity = comparator.compare(swfCloud1, swfCloud2);
			
			// Do some analysis
			if (DendrogramTagCloudAnalyzer.ANALYSIS) {
				if ((1.0 - similarity) < 0.3) {
					tagCloudsFw.write("Similarity > 0.7 at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(parentCloud.toString() + "\n\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
				if ((1.0 - similarity) > 0.8) {
					tagCloudsFw.write("Similarity < 0.2 at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(parentCloud.toString() + "\n\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
			}
			
			fw.write(similarity.toString() + "\n");
			parent = parent.parent();
			depth++;
		}
		tagCloudsFw.close();
		fw.close();
		R r = new R(this.logger);
		// We set y-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("cosineSimilarity_stopWords_" + i + ".pdf") + " cosine_similarity"
		);
	}



	private void plotNormalizedSetDifferenceWithStopWords(int i) throws Exception {
		this.logger.log("Start writing normalized top-n set-difference for leave " + i);
		NormalizedSetDifferenceTagComparatorStrategy comparatorStrategy = new NormalizedSetDifferenceTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "normalizedTopNSetDifference_stopWords_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		FileWriter tagCloudsFw = this.fileManager.getNewResultFileWriter("tagCloudsWithSimilarity0_leaf" + i + ".txt");
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		int maxDepth = leaf.depth();
		while(parent != null) {
			this.logger.log("Analyzing node at depth " + depth + " of " + maxDepth);
			
			// Filter stopwords from given clouds
			StopWordFilteredTagCloud swfCloud1 = new StopWordFilteredTagCloud(parent.dendrogram1().tagCloud());
			StopWordFilteredTagCloud swfCloud2 = new StopWordFilteredTagCloud(parent.dendrogram2().tagCloud());
			
			Double similarity = comparator.compare(swfCloud1, swfCloud2);
			
			// Do some analysis
			if (DendrogramTagCloudAnalyzer.ANALYSIS) {
				if ((1.0 - similarity) < 0.0001) {
					tagCloudsFw.write("Equivalent tag clouds at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
				if ((1.0 - similarity) > 0.9999) {
					tagCloudsFw.write("Disjunct tag clouds at depth " + depth + " (similarity: " + similarity + ")\n");
					tagCloudsFw.write(swfCloud1.toString() + "\n\n");
					tagCloudsFw.write(swfCloud2.toString() + "\n\n\n");
				}
			}
			
			fw.write(similarity.toString() + "\n");
			parent = parent.parent();
			depth++;
		}
		tagCloudsFw.close();
		fw.close();
		R r = new R(this.logger);
		// We set x-axis label as 3rd parameter
		r.run(
				"TagCloudNormalizedTopNSetDifference.r", 
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedSetDifference_stopWords_" + i + ".pdf") + " \"Normalized top-n set-difference\""
		);
	}



	private void plotNormalizedTopNSetDifference(int i) throws Exception {
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
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedTopNSetDifference_" + i + ".pdf") + " \"Normalized top-n set-difference\""
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
			
			if (DendrogramTagCloudAnalyzer.ANALYSIS) {
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
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("normalizedSetDifference_" + i + ".pdf") + " \"Normalized set-difference\""
		);
	}



	private void plotSetDifference(int i) throws Exception {
		SetDifferenceTagComparatorStrategy comparatorStrategy = new SetDifferenceTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "setDifference_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		while(parent != null) {
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
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("setDifference_" + i + ".pdf") + " \"Set-difference\""
		);
	}



	private void plotCosinSimilarity(int i) throws Exception {
		CosineSimilarityTagComparatorStrategy comparatorStrategy = new CosineSimilarityTagComparatorStrategy();
		TagCloudComparator comparator = new TagCloudComparator(comparatorStrategy);
		LeafDendrogram<RecommenderObject> leaf = this.leaves[i];
		String fileName = "cosineSimilarity_" + i + ".txt";
		String tmpFilePath = this.fileManager.getTempFilePath(fileName);
		FileWriter fw = this.fileManager.getNewTempFileWriter(fileName);
		LinkDendrogram<RecommenderObject> parent = leaf.parent();
		int depth = 1;
		while(parent != null) {
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
				tmpFilePath + " " + this.fileManager.getPlotsFilePath("cosineSimilarity_" + i + ".pdf") + " \"cosine similarity\""
		);
	}
	

}
