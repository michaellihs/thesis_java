package de.mknoll.thesis.datastructures.graph.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.DefaultIdProvider;
import de.mknoll.thesis.datastructures.graph.IdProvider;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a writer that exports a graph into an edgelist format
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class EdgeListWriter implements GraphWriter {
	
	/**
	 * Holds ID provider which gets an id from a given recommendation
	 * @Inject
	 */
	private IdProvider idProvider;
	
	
	
	/**
	 * Holds delimiter string for separating vertices in edge list
	 */
	private String delimiter = "\t";
	
	
	
	/**
	 * Holds delimiter string for new line in edge list
	 */
	private String newLine = "\n";
	
	
	
	/**
	 * Holds a logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Constructor for edgelist writer.
	 * 
	 * configuration expects the following values to be set:
	 * - exportFilePath = full qualified path to write edgelist file to
	 * 
	 * TODO add optional parameters for
	 * - delimiter = string to separate vertices in edgelist
	 * - newline = string to separate one line from another
	 * - idProviderClass = class name of id provider
	 * 
	 * @param configuration
	 */
	public EdgeListWriter(LoggerInterface logger, IdProvider idProvider) {
		this.logger = logger;
		this.idProvider = idProvider;
	}
	
	
	
	/**
	 * Writes given graph into edgelist file given by destination
	 * 
	 * @param Graph to be written into edgelist file
	 * @param Path of edgelist file to write graph to
	 * @throws Exception
	 * @Override 
	 */
	 public void write(RecommendationGraph graph, String destination) throws Exception {
		 
		// Try to get export path 
		String fileName = destination;
		if (fileName == null) {
			throw new Exception("No exportFileName given in edgelist-writer configuration.");
		}
		
		// Write edgelist to export path
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			
			this.logger.log("Start writing edgelist to " + destination + "\n");
			this.writeGraphToStream(graph, out);
			this.logger.log("Finished writing edgelist to file");
			
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			this.logger.log("Error while trying to write graph to edgelist: " + e.getMessage() + "\n" + e.getStackTrace().toString());
		}
		
	}
	 
	 
	
	/**
	 * Writes all edges for given recommendation graph into given writer
	 * 
	 * @param graph Recommendation graph to export edges from
	 * @param out Writer to write edges to
	 * @throws IOException Throws exception, if unable to write
	 */
	protected void writeGraphToStream(RecommendationGraph graph, Writer out) throws IOException {
		for(Recommendation recommendation : graph.getAllRecommendations()) {
			String edge = this.idProvider.getId(recommendation.getSourceRecommendation()) + 
					this.delimiter + 
					this.idProvider.getId(recommendation.getTargetRecommendation()) + 
					this.newLine;
			// this.logger.log("Writing edge: " + edge);
			out.write(edge);
		}
	}

}
