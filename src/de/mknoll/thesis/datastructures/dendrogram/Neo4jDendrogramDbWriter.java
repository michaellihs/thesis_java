package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashMap;

import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestAPI;

import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Dendrogram writer for writing dendrograms into Neo4J graph database
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.dendrogram.Neo4jDendrogramWriterTest
 */
public class Neo4jDendrogramDbWriter<T> {
	
	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Holds graph database API for manipulating neo4j database
	 */
	private RestAPI restApi;
	
	
	
	/**
	 * Holds mapping of ids to nodes
	 */
	private IdNodeMap idNodeMap;
	
	
	
	/**
	 * Constructor takes logger as argument
	 * 
	 * @param logger
	 */
	public Neo4jDendrogramDbWriter(LoggerInterface logger, RestAPI restApi, IdNodeMap idNodeMap) {
		this.logger = logger;
		this.restApi = restApi;
		this.idNodeMap = idNodeMap;
	}

	
	
	/**
	 * Writes given dendrogram into neo4j database
	 * 
	 * @param dendrogram
	 * @return
	 */
	public void write(Dendrogram<T> dendrogram) {
		if (dendrogram.isLeaf()) {
			// dendrogram consists of a single leaf node
			this.createNodeForLeaf((LeafDendrogram<T>)dendrogram);
		} else {
			// dendrogram contains at least to objects
			Node node = this.createNodeForLink((LinkDendrogram<T>)dendrogram);
			this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram1());
			this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram2());
		}
	}
	
	
	
	private Node writeNode(Node currentNode, Dendrogram<T> dendrogram) {
		Node node = this.createNodeForDendrogram(dendrogram);
		this.createIsMergedIntoRelationship(node, currentNode);
		this.createContainsRelationship(currentNode, node);
		if (dendrogram.isLink()) {
			this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram1());
			this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram2());
		}
		return node;
	}
	
	
	
	private void createContainsRelationship(Node clusterNode, Node containedNode) {
		this.restApi.createRelationship(clusterNode, containedNode, RecommenderRelationshipTypes.CONTAINS, null);
	}



	private void createIsMergedIntoRelationship(Node containedNode, Node clusterNode) {
		this.restApi.createRelationship(containedNode, clusterNode, RecommenderRelationshipTypes.IS_MERGED_INTO, null);
	}



	private Node createNodeForDendrogram(Dendrogram<T> d) {
		if (d.isLeaf()) {
			return this.createNodeForLeaf((LeafDendrogram<T>)d);
		} else {
			return this.createNodeForLink((LinkDendrogram<T>)d);
		}
	}

	
	
	private Node createNodeForLink(LinkDendrogram<T> dendrogram) {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("cluster_size", dendrogram.size());
		return this.restApi.createNode(properties);
	}



	private Node createNodeForLeaf(LeafDendrogram<T> dendrogram) {
		RecommenderObject recObject = (RecommenderObject)dendrogram.object();
		de.mknoll.thesis.datastructures.graph.Node node = this.idNodeMap.getNodeByNamespaceAndExternalId(DefaultNamespaces.BIBTIP, recObject.getDocId());
		Node n4jNode = this.restApi.getNodeById(Long.parseLong(node.getExternalIdByNamespace(DefaultNamespaces.NEO4J)));
		return n4jNode;
	}
	
}
