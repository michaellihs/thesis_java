package de.mknoll.thesis.datastructures.dendrogram;

import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Class implements a dendrogram builder for Newman fast modularity clustering
 * 
 * When merging Cluster A with Cluster B in Newman algorithm, we have
 * 
 * Id(Cluster(A+B)) = Id(Cluster B)
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 *
 */
public class NewmanDendrogramBuilder extends RecommenderObjectDendrogramBuilder {

	public NewmanDendrogramBuilder(IdNodeMap map) {
		super(map);
	}
	
	
	
	/**
	 * Merges two dendrograms identified by given ids
	 * 
	 * An id is an external (generated) ID here!
	 * 
	 * @param Id to be found within first dendrogram to be merged
	 * @param Id to be found within second dendrogram to be merged
	 * @throws Exception 
	 */
	public void mergeByIds(String id1, String id2) throws Exception {
		Dendrogram<RecommenderObject> mergedDendrogram = this.mergeDendrograms(
				this.getDendrogramById(id1),
				this.getDendrogramById(id2)
		);
		this.idToDendrogramMap.remove(id1);
		this.idToDendrogramMap.remove(id2);
		
		// We set ID of merged cluster to be ID of second cluster to be merged
		this.idToDendrogramMap.put(id2, mergedDendrogram);
	}
	
}
