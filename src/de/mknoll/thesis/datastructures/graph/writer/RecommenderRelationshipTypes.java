package de.mknoll.thesis.datastructures.graph.writer;

import org.neo4j.graphdb.RelationshipType;



/**
 * Enum for relationship types used within recommender graph.
 * 
 * @see http://api.neo4j.org/1.8.M01/org/neo4j/graphdb/RelationshipType.html
 * 
 * @author mimi
 */
public enum RecommenderRelationshipTypes implements RelationshipType {
	IS_RECOMMENDATION_FOR, 		// Use this for relation A -> B if B is a recommendation for A
	IS_RECOMMENDED_BY, 			// Use this for relation A -> B if A is recommended by B
	IS_MERGED_INTO,				// Use this for relation A -> B if B is a cluster into which A is merged
	CONTAINS					// Use this for relation A -> B if B is contained within cluster A
}
