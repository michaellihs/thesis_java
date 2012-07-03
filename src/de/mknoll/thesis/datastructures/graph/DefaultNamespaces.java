package de.mknoll.thesis.datastructures.graph;



/**
 * Enumeration of ID namespaces.
 * 
 * Each node within recommendation graph or dendrogram can have several IDs.
 * IDs are set within several namespaces which are defined here.
 * 
 * Within BIBTIP recommendation graph, a node can have ID ol:1234
 * Within NEO4J graph database, a node has a numeric ID like 1234
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public enum DefaultNamespaces implements Namespaces {

	BIBTIP, NEO4J, ISBN12;
	
}
