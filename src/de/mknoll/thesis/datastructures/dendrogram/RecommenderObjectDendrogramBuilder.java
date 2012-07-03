package de.mknoll.thesis.datastructures.dendrogram;

import de.mknoll.thesis.datastructures.graph.IdentifierRecommenderObjectMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements a dendrogram builder for building 
 * dendrograms of recommender objects. 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class RecommenderObjectDendrogramBuilder extends DendrogramBuilder<RecommenderObject> {
	
	/**
	 * Holds mapping of internal / external IDs and recommender objects
	 */
	private IdentifierRecommenderObjectMap map;
	
	
	
	/**
	 * Constructor takes identifier mapping
	 *  
	 * @param map
	 */
	public RecommenderObjectDendrogramBuilder(IdentifierRecommenderObjectMap map) {
		super();
		this.map = map;
	}
	
	

	@Override
	protected RecommenderObject getInstanceOfTypeObject(String id) {
		return this.map.getRecommenderObjectByExternalId(id);
	}
	
	

	@Override
	protected String getIdFromObject(RecommenderObject object) {
		return this.map.getExternalIdByRecommenderObject(object);
	}

}
