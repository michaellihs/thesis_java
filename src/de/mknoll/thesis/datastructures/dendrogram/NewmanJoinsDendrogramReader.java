package de.mknoll.thesis.datastructures.dendrogram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a reader that creates a dendrogram from
 * Newman's joins files as created by Fast Modularity clustering
 * algorithm.
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 *
 */
public class NewmanJoinsDendrogramReader {

	/**
	 * Holds path to file to be read
	 */
	private String filePath;
	
	
	
	/**
	 * Holds optional id mapping
	 */
	private HashMap<String, String> idMapping;
	
	
	
	/**
	 * Holds regex pattern for matching IDs in a single joins file line
	 */
	private Pattern joinsFilePattern;
	
	
	
	/**
	 * Holds dendrogram builder for building dendrograms with recommender objects
	 */
	private RecommenderObjectDendrogramBuilder dendrogramBuilder;
	
	
	
	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Constructor takes dendrogramBuilder as argument
	 * 
	 * @param dendrogramBuilder
	 */
	public NewmanJoinsDendrogramReader(RecommenderObjectDendrogramBuilder dendrogramBuilder, LoggerInterface logger) {
		this.dendrogramBuilder = dendrogramBuilder;
		this.logger = logger;
	}
	
	
	
	/**
	 * Reads dendrogram from given file with id mapping.
	 * 
	 * Mapping is used to convert internal IDs used in input file into RecommenderObject IDs used in database
	 * 
	 * @param Path to file to be read
	 * @param Mapping of internal to external IDs
	 * @return Dendrogram from given input file
	 * @throws IOException 
	 */
	public Dendrogram<RecommenderObject> read(String filePath, HashMap<String, String> idMapping) throws Exception {
		this.idMapping = idMapping;
		return this.read(filePath);
	}
	
	
	
	/**
	 * Reads dendrogram from given file
	 * 
	 * @param Path to file to be read
	 * @return Dendrogram from given input file
	 * @throws IOException 
	 */
	public Dendrogram<RecommenderObject> read(String filePath) throws Exception {
		this.filePath = filePath;
		this.initPatternMatcher();
		Dendrogram<RecommenderObject> dendrogram = this.readFromFile();
		return dendrogram;
	}



	/**
	 * Initializes pattern for matching a single line in .joins file
	 */
	private void initPatternMatcher() {
		this.joinsFilePattern = Pattern.compile("(\\d+)\\s+(\\d+)");
	}



	/**
	 * Reads a dendrogram from a file and returns it.
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Dendrogram<RecommenderObject> readFromFile() throws Exception {
		// TODO think about how we get a new empty dendrogram
		FileInputStream dendrogramFileInputStream = new FileInputStream(this.filePath);
		InputStreamReader dendrogramInputStreamReader = new InputStreamReader(dendrogramFileInputStream);
		Scanner dendrogramInputStreamScanner = new Scanner(dendrogramFileInputStream); 
		while (dendrogramInputStreamScanner.hasNextLine()) {
			this.addJoinToDendrogramByLine(dendrogramInputStreamScanner.nextLine());
		}
		dendrogramInputStreamScanner.close();
		dendrogramInputStreamReader.close();
		dendrogramFileInputStream.close();
		
		for (Dendrogram<RecommenderObject> dendrogram: dendrogramBuilder.getDendrograms()) {
			this.logger.log("Got dendrogram with size " + dendrogram.size());
		}
		
		Dendrogram<RecommenderObject> biggestDendrogram = this.dendrogramBuilder.getBiggestDendrogram(); 
		this.logger.log("Biggest dendrogram has size " + biggestDendrogram.size());
		
		return biggestDendrogram; 
	}



	/**
	 * Reads a given line and adds it to given dendrogram
	 * 
	 * A line in .joins file looks similar to this
	 * 2695    42      0.477945        7970
	 * <firstId>  <secondId>  <maxModularity>  <timeStep>
	 * 
	 * @param nextLine
	 */
	private void addJoinToDendrogramByLine(String line) {
		// for some reason, first line of joins starts with "-1 -1" so we step over
		if (!(line.startsWith("-1"))) {
			Matcher joinsFileMatcher = this.joinsFilePattern.matcher(line);
			joinsFileMatcher.find();
			String absorberId = joinsFileMatcher.group(1);
			String absorbedId = joinsFileMatcher.group(2);
			this.dendrogramBuilder.mergeByIds(absorberId, absorbedId);
		}
	}
	
}
