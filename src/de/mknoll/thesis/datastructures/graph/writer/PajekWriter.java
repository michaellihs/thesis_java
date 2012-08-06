package de.mknoll.thesis.datastructures.graph.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.picocontainer.annotations.Inject;

import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
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
public class PajekWriter implements GraphWriter {
	
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
			this.logger.log("Error while trying to write graph to pajek format: " + e.getMessage() + "\n" + e.getStackTrace().toString());
		}
		
	}



	private void writeGraphToStream(RecommendationGraph graph, BufferedWriter out) throws IOException {
		// Write a little comment denoting |edges|
		out.write("*Vertices " + graph.vertexSet().size() + "\n");
		
		// Write list of vertices
		for (Node n : graph.vertexSet()) {
			out.write(n.internalId() + " \"" + ((RecommenderObject)n.getAttachedObject(RecommenderObject.NODE_IDENTIFIER)).getDescription() + "\"\n");
		}
		
		// Write a little comment denoting |edges|
		 out.write("*Edges: " + graph.edgeSet().size() + "\n");
		 for (Recommendation r : graph.edgeSet()) {
			 out.write(r.getSource().internalId() + " " + r.getTarget().internalId() + " " + r.getWeight() + "\n");
		 }
	}

}
