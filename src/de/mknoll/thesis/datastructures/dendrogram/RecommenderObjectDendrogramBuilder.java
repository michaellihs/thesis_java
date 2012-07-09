package de.mknoll.thesis.datastructures.dendrogram;

import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements a dendrogram builder for building 
 * dendrograms of recommender objects. 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
abstract public class RecommenderObjectDendrogramBuilder extends DendrogramBuilder<RecommenderObject> {
	
	/**
	 * Holds mapping of internal / external IDs and recommender objects
	 */
	private IdNodeMap map;
	
	
	
	/**
	 * Constructor takes identifier mapping
	 *  
	 * @param map
	 */
	public RecommenderObjectDendrogramBuilder(IdNodeMap map) {
		super();
		this.map = map;
	}
	
	

	@Override
	protected RecommenderObject getInstanceOfTypeObject(String id) throws Exception {
		Node node = this.map.getNodeByInternalId(Integer.parseInt(id));
		if (node == null) {
			throw new Exception("Trying to get node by internal id " + id + " from mapping, but no node could be retrieved!");
		}
		RecommenderObject recObj = (RecommenderObject) node.getAttachedObject(RecommenderObject.NODE_IDENTIFIER);
		return recObj;
	}
	
	

	@Override
	protected String getIdFromObject(RecommenderObject object) {
		String externalId = this.map.getInternalIdByNamespaceAndExternalId(DefaultNamespaces.BIBTIP, object.getDocId()).toString();
		return externalId;
	}

}
