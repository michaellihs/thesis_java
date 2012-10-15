package de.mknoll.thesis.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.dendrogram.Dendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.CosineSimilarityTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.StemmedStopWordFilteredTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.StopWordFilteredTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudComparator;
import de.mknoll.thesis.externaltools.wrapper.R;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.util.Pair;



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
	
	
	
	/**
	 * Holds file manager to get paths for result files
	 */
	private FileManager fileManager;
	
	
	
	/**
	 * Holds a logger to output some debug messages
	 */
	private LoggerInterface logger;



	/**
	 * Holds dendrogram to be analyzed
	 */
	private Dendrogram<RecommenderObject> dendrogram;



	/**
	 * Holds a set of leaves that should be analyzed
	 */
	private LeafDendrogram<RecommenderObject>[] leaves;



	/**
	 * Holds a map of tag could comparators which should be used to compare tag clouds
	 */
	private Map<String, TagCloudComparator> comparatorMap;
	
	
	
	/**
	 * Holds a map of file writers each with an individual name to write results
	 */
	private Map<String, FileWriter> fileWriterMap;



	/**
	 * Holds number of tags to be taken in calculation for top-n set difference
	 */
	private int topN = 10;


	
	/**
	 * If set to true, a new step in dendrogram depth is processed
	 * 
	 * TODO think about better way to handle this
	 */
	private boolean nextStep;
	
	
	
	/**
	 * Constructor gets some dependencies injected
	 * 
	 * @param fileManager File manager to be used for result files
	 * @param logger Logger to be used for writing some debug messages
	 */
	public DendrogramTagCloudAnalyzer(FileManager fileManager, LoggerInterface logger) {
		this.fileManager = fileManager;
		this.logger = logger;
		this.fileWriterMap = new HashMap<String, FileWriter>();
	}
	
	
	/**
	 * Runs analyzes on given dendrogram and a given set of leaves, encoded with their id within the dendrogram
	 * 
	 * @param dendrogram Dendrogram to be analyzed
	 * @param leaves Array of leaves to be used for analyzes
	 * @throws Exception
	 * @SuppressWarnings("unchecked")
	 */
	public void analyzeTagCloudsByLeavesFor(Dendrogram<RecommenderObject> dendrogram, int[] leaves) throws Exception {
		this.dendrogram = dendrogram;
		this.setUpComparators();
		
		this.leaves = (LeafDendrogram<RecommenderObject>[])this.dendrogram.leaves().toArray(new LeafDendrogram[this.dendrogram.leaves().size()]);
		for (int i = 0; i < leaves.length; i++) {
			this.analyzeTagCloudsForGivenLeaf(leaves[i]);
		}
		this.closeFileWriters();
		for (int i = 0; i < leaves.length; i++) {
			this.drawPlots(i);
		}
	}



	private void closeFileWriters() throws IOException {
		for (String name : this.fileWriterMap.keySet()) {
			this.fileWriterMap.get(name).close();
		}
	}



	private void analyzeTagCloudsForGivenLeaf(int i) throws Exception {
		Dendrogram<RecommenderObject> child = this.leaves[i];
		Dendrogram<RecommenderObject> parent = child.parent();
		
		int currentDepth = 1;
		int maxDepth = child.depth();
		
		while (parent != null) {
			this.nextStep = true;
			List<Pair<String, Pair<Cloud, Cloud>>> cloudPairs = this.getCloudPairs(child, parent);
			
			for (Pair<String, Pair<Cloud, Cloud>> cloudPair : cloudPairs) {
				
				for (String comparatorName : this.comparatorMap.keySet()) {
					this.compare(comparatorName, cloudPair, i, currentDepth, maxDepth);
				}
				
			}
			
			child = parent;
			parent = parent.parent();
			currentDepth++;
		}
		
		/*
		
		
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
		
		*/
	}



	private void drawPlots(int i) throws Exception {
		// Draw single plots
		for (String fileName : this.fileWriterMap.keySet()) {
			String tmpFile = this.fileManager.getTempFilePath(fileName + ".txt");
			String plotFile = this.fileManager.getPlotsFilePath(fileName + ".pdf");
			R r = new R(this.logger);
			// We set x-axis label as 3rd parameter
			r.run(
					"TagCloudNormalizedTopNSetDifference.r", 
					tmpFile + " " + plotFile + " similarity" 
			);
		}
		
		// TODO how to plot multi-plots???
	}



	/**
	 * Compares given tag cloud pair using given comparator
	 * 
	 * @param comparatorName
	 * @param cloudPair
	 * @param leaveNumber
	 * @param step
	 * @param maxDepth
	 * @throws Exception
	 */
	private void compare(String comparatorName, Pair<String, Pair<Cloud, Cloud>> cloudPair, int leaveNumber, int step, int maxDepth) throws Exception {
		/**
		 * What should be done here:
		 * 
		 * 1. Get a result file writer for string given in pair (nonFiltered, stop-word...) and comparatorName
		 * 2. Compare tag clouds given in pair using comparator given in comparator Name
		 * 3. Write special statistics (like equal tags...) into special files using stats-writer method (to be implemented)
		 */
		TagCloudComparator comparator = this.comparatorMap.get(comparatorName);
		FileWriter compareFileWriter = this.getFileWriterByFilteringAndComparator(leaveNumber, cloudPair.getFirst(), comparatorName); // TODO to be implemented
		Double similarity = comparator.compare(cloudPair.getSecond().getFirst(), cloudPair.getSecond().getSecond());
		compareFileWriter.write(similarity.toString() + "\n");
		this.writeAdditionalInformation(leaveNumber, comparatorName, cloudPair, step, similarity);
	}



	/**
	 * Writes additional information for given cloud pairs
	 * 
	 * @param leaveNumber Number of leave that is currently analyzed
	 * @param comparatorName Name of comparator that is currently used
	 * @param cloudPair Pair of tag clouds to be compared
	 * @param step Depth of dendrogram currently analyzed
	 * @param similarity Similarity calculated by comparator
	 * @throws Exception 
	 */
	private void writeAdditionalInformation(
			int leaveNumber, 
			String comparatorName, 
			Pair<String, Pair<Cloud, Cloud>> cloudPair,	
			int step, 
			Double similarity) throws Exception {
		
		// TODO adding a prefix to comparator name is a kind of a hack...
		FileWriter fw = this.getFileWriterByFilteringAndComparator(leaveNumber, "", "_additionalIformation");

		/** 
		 * How should the report look like that we write here:
		 * 
		 * - Name of similarity measure
		 * - step (depth in dendrogram)
		 * - Pair of compared cloud
		 * - Difference in compared cloud (words that are included in first cloud, but not in second)
		 * - similarity
		 * 
		 */
		if (this.nextStep) {
			fw.write("STEP: " + step + "\n \n");
			//fw.write("Parent tag cloud: \n" + "=================\n" + cloudPair.getSecond().getFirst().toString() + "\n\n");
			//fw.write("Child tag cloud: \n" + "================\n" + cloudPair.getSecond().getSecond().toString() + "\n\n");
			
			fw.write("Parent Cloud Size: " + cloudPair.getSecond().getFirst().size() + "\n");
			fw.write("Child Cloud Size: " + cloudPair.getSecond().getSecond().size() + "\n\n");
			fw.write("Tag Difference: " + "\n" + "===============" + "\n\n" + new DefaultTagCloud(cloudPair.getSecond().getFirst().getDifference(cloudPair.getSecond().getSecond())).toString() + "\n\n");
			
			this.nextStep = false;
		}
		
		fw.write(cloudPair.getFirst() + " -- ");
		fw.write(comparatorName + " -- ");
		fw.write("Similarity: " + similarity + "\n");
	}

	


	/**
	 * Returns fileWriter for given parameters
	 * 
	 * @param leaveNumber
	 * @param filterName
	 * @param comparatorName
	 * @return FileWriter for given parameters
	 * @throws Exception
	 */
	private FileWriter getFileWriterByFilteringAndComparator(int leaveNumber, String filterName, String comparatorName) throws Exception {
		String fileName = this.getFileNameByFilteringAndComparator(leaveNumber, filterName, comparatorName);
		if (!this.fileWriterMap.containsKey(fileName)) {
			this.fileWriterMap.put(fileName, this.fileManager.getNewTempFileWriter(fileName + ".txt"));
		}
		return this.fileWriterMap.get(fileName);
	}
	
	
	
	/**
	 * Creates a file name for a given set of parameters to describe result file
	 * 
	 * @param leaveNumber
	 * @param filterName
	 * @param comparatorName
	 * @return File name for given parameters
	 */
	private String getFileNameByFilteringAndComparator(int leaveNumber, String filterName, String comparatorName) {
		return filterName + "_" + comparatorName + "_" + leaveNumber;
	}



	/**
	 * Generates an array of cloud pairs for each given child and parent
	 * 
	 * We can set up pairs of different tag clouds here, depending on what kinds of clouds we want to compare.
	 * E.g. non-filtered clouds, stop-word filtered clouds, stemmed clouds...
	 * 
	 * @param child Cluster contained by parent
	 * @param parent Cluster containing child
	 * @return Named pairs of clouds for parent and child (like no filtering, stop-word filtering, stemming...)
	 */
	private List<Pair<String, Pair<Cloud, Cloud>>> getCloudPairs(Dendrogram<RecommenderObject> child, Dendrogram<RecommenderObject> parent) {
		List<Pair<String, Pair<Cloud, Cloud>>> cloudPairs = new ArrayList<Pair<String,Pair<Cloud,Cloud>>>();
		
		// Non filtered clouds
		cloudPairs.add(new Pair<String, Pair<Cloud,Cloud>>("nonFiltered", new Pair<Cloud, Cloud>(child.tagCloud(), parent.tagCloud())));
		
		// Stop-word filtered clouds
		Cloud swfCloud1 = new StopWordFilteredTagCloud(child.tagCloud());
		Cloud swfCloud2 = new StopWordFilteredTagCloud(parent.tagCloud());
		cloudPairs.add(new Pair<String, Pair<Cloud,Cloud>>("stopWordFiltered", new Pair<Cloud, Cloud>(swfCloud1, swfCloud2)));
		
		// Stemmed stop-word filtered clouds
		Cloud stemSwfCloud1 = new StemmedStopWordFilteredTagCloud(child.tagCloud());
		Cloud stemSwfCloud2 = new StemmedStopWordFilteredTagCloud(parent.tagCloud());
		cloudPairs.add(new Pair<String, Pair<Cloud,Cloud>>("stemmedStopWordFiltered", new Pair<Cloud, Cloud>(stemSwfCloud1, stemSwfCloud2)));
		
		return cloudPairs;
	}



	private void setUpComparators() {
		this.comparatorMap = new HashMap<String, TagCloudComparator>();
		
		// Set up set difference comparator
		SetDifferenceTagComparatorStrategy sdComparatorStrategy = new SetDifferenceTagComparatorStrategy();
		TagCloudComparator sdComparator = new TagCloudComparator(sdComparatorStrategy);
		this.comparatorMap.put("setDifferenceComparator", sdComparator);
		
		// Set up normalized set difference comparator
		NormalizedSetDifferenceTagComparatorStrategy nsdComparatorStrategy = new NormalizedSetDifferenceTagComparatorStrategy();
		TagCloudComparator nsdComparator = new TagCloudComparator(nsdComparatorStrategy);
		this.comparatorMap.put("normalizedSetDifferenceComparator", nsdComparator);
		
		// Set up top-n normalized set difference comparator
		NormalizedSetDifferenceTopNTagComparatorStrategy ntnsdComparatorStrategy = new NormalizedSetDifferenceTopNTagComparatorStrategy(this.topN );
		TagCloudComparator ntnsdComparator = new TagCloudComparator(ntnsdComparatorStrategy);
		this.comparatorMap.put("normalizedTopNSetDifferenceComparator", ntnsdComparator);
		
		// Set up cosine similarity comparator
		CosineSimilarityTagComparatorStrategy csComparatorStrategy = new CosineSimilarityTagComparatorStrategy();
		TagCloudComparator csComparator = new TagCloudComparator(csComparatorStrategy);
		this.comparatorMap.put("cosineSimilarityComparator", csComparator);
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
			child = parent;
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
