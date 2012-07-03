package de.mknoll.thesis.datastructures.graph.writer;

import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;

import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements a graph writer for graphml format.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class GraphmlWriter implements GraphWriter {
	
	
	

	@Override
	public void write(RecommendationGraph graph, String destination) {
		
		GraphMLExporter exporter = new GraphMLExporter<RecommenderObject, Recommendation>(
				new RecommendationVertexIdProvider(),
				new RecommendationVertexNameProvider(),
				new RecommendationIdProvider(),
				new RecommendationNameProvider()
		);
		

		
		
	}
	
	
	
	private class RecommendationVertexNameProvider implements VertexNameProvider<RecommenderObject> {

		@Override
		public String getVertexName(RecommenderObject arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	private class RecommendationVertexIdProvider implements VertexNameProvider<RecommenderObject> {

		@Override
		public String getVertexName(RecommenderObject arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	private class RecommendationIdProvider implements EdgeNameProvider<Recommendation> {

		@Override
		public String getEdgeName(Recommendation arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	
	
	private class RecommendationNameProvider implements EdgeNameProvider<Recommendation> {

		@Override
		public String getEdgeName(Recommendation arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
