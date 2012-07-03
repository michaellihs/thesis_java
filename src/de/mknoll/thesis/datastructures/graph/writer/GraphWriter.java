package de.mknoll.thesis.datastructures.graph.writer;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Interface for graph writers
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface GraphWriter {

	/**
	 * Writes given graph to given destination
	 * 
	 * @param Graph to be written to destination
	 * @param Destination to write graph to
	 * @throws Exception
	 */
	public void write(RecommendationGraph graph, String destination) throws Exception;
	
}
