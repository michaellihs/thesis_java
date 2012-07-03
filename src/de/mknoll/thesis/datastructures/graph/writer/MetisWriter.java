package de.mknoll.thesis.datastructures.graph.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.DefaultIdProvider;
import de.mknoll.thesis.datastructures.graph.IdProvider;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.UniqueIdProvider;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Class implements a writer that exports a graph into an Metis format
 * 
 * For further information on Metis format 
 * @see http://people.sc.fsu.edu/~jburkardt/data/metis_graph/metis_graph.html
 * 
 * @see de.mknoll.thesis.tests.datastructures.graph.writer.MetisWriter
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class MetisWriter implements GraphWriter {
	
	@Inject
	LoggerInterface logger;
	
	

	@Override
	public void write(RecommendationGraph graph, String destination)
			throws Exception {
		
		try {
			FileWriter fstream = new FileWriter(destination);
			BufferedWriter out = new BufferedWriter(fstream);
			
			this.writeGraphToStream(graph, out);
			
			//Close the output stream
			out.close();
			
		}catch (Exception e){//Catch exception if any
			this.logger.log("Error while trying to write graph to edgelist: " + e.getMessage() + "\n" + e.getStackTrace().toString());
		}
		
	}



	private void writeGraphToStream(RecommendationGraph graph, BufferedWriter out) throws IOException {
		// First line of Metis needs to be #vertices #edges
		out.write(graph.vertexSet().size() + " " + graph.edgeSet().size() + "\n");
		for(Node n: graph.vertexSet()) {
			this.writeNodeListToStream(graph, n, out);
		}
	}



	private void writeNodeListToStream(RecommendationGraph graph, Node n, BufferedWriter out) throws IOException {
		boolean notFirst = false;
		for (Node neighbor : graph.getNeighborsOf(n)) {
			if (notFirst) {
				out.write(" ");
			}
			out.write(new Integer(neighbor.internalId()).toString());
			notFirst = true;
		}
		out.write("\n");
	}

}
