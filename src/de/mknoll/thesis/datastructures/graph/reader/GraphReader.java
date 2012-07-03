package de.mknoll.thesis.datastructures.graph.reader;

import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Interface for graph readers
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface GraphReader {

	public RecommendationGraph read() throws Exception;
	
}
