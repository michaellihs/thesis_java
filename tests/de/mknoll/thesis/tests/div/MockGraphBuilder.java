package de.mknoll.thesis.tests.div;

import static org.junit.Assert.fail;
import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.UniqueNodeIdProvider;



/**
 * Class implements a graph builder with some methods for building 
 * recommender graph mocks used for testing.
 * 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class MockGraphBuilder {

	public static RecommendationGraph createRecommendationTestGraph() {
		return createRecommendationTestGraph(true);
	}
	
	
	
	public static RecommendationGraph createRecommendationTestGraph(boolean resetUniqueIdProvider) {
		if (resetUniqueIdProvider) {
			UniqueNodeIdProvider.getInstance().reset();
		}
		IdNodeMap map = new DefaultIdNodeMap();
		
		RecommendationGraph recGraph = new RecommendationGraph(map);
		
		RecommenderObject recObject1 = new RecommenderObject("id:1", "My first recommender object", "123456789012");
		RecommenderObject recObject2 = new RecommenderObject("id:2", "My second recommender object", "123456789013");
		RecommenderObject recObject3 = new RecommenderObject("id:3", "My third recommender object", "123456789014");
		RecommenderObject recObject4 = new RecommenderObject("id:4", "My fourth recommender object", "123456789015");
		RecommenderObject recObject5 = new RecommenderObject("id:5", "My fifth recommender object", "123456789016");
		RecommenderObject recObject6 = new RecommenderObject("id:6", "My sixth recommender object", "123456789017");
		
		try {
			recGraph.addRecommendation(recObject1, recObject2, 1);
			recGraph.addRecommendation(recObject1, recObject3, 1);
			recGraph.addRecommendation(recObject1, recObject4, 1);
			recGraph.addRecommendation(recObject2, recObject3, 1);
			recGraph.addRecommendation(recObject2, recObject4, 1);
			recGraph.addRecommendation(recObject2, recObject5, 1);
			recGraph.addRecommendation(recObject3, recObject4, 1);
			recGraph.addRecommendation(recObject3, recObject5, 1);
			recGraph.addRecommendation(recObject4, recObject6, 1);
			recGraph.addRecommendation(recObject5, recObject6, 1);
		} catch (Exception e) {
			fail("An exception has been thrown when trying to add recommendations to graph. This shouldn't have happened!");
		}
		
		return recGraph;
	}
	
}
