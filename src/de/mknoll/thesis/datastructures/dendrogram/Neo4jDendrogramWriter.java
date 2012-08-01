package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashMap;

import org.neo4j.graphdb.Node;

import de.mknoll.thesis.datastructures.graph.AttachableToNode;
import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.graph.writer.RecommenderRelationshipTypes;
import de.mknoll.thesis.datastructures.tagcloud.CosineSimilarityTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudComparator;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.neo4j.Neo4jWriter;



/**
 * Dendrogram writer for writing dendrograms into Neo4J graph database
 * 
 * Whether we use neo4j file storage or neo4j databse storage is handled
 * by given neo4j writer.
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.dendrogram.Neo4jDendrogramWriterTest
 */
public class Neo4jDendrogramWriter<T extends TagCloudContainer & AttachableToNode> {
	
	/**
	 * Holds n4j writer used for writing nodes and relationship to database
	 */
	private Neo4jWriter writer;
	
	
	
	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Holds mapping of ids to nodes
	 */
	private IdNodeMap idNodeMap;
	
	
	
	private TagCloudComparator setDifferenceComperator;
	
	
	
	private TagCloudComparator normalizedSetDifferenceComperator;
	
	
	
	private TagCloudComparator cosineSimilarityComperator;
	
	
	
	/**
	 * Constructor takes logger as argument
	 * 
	 * @param logger
	 */
	public Neo4jDendrogramWriter(LoggerInterface logger, Neo4jWriter writer, IdNodeMap idNodeMap) {
		this.logger = logger;
		this.writer = writer;
		this.idNodeMap = idNodeMap;
		
		this.setDifferenceComperator = new TagCloudComparator(new SetDifferenceTagComparatorStrategy());
		this.normalizedSetDifferenceComperator = new TagCloudComparator(new NormalizedSetDifferenceTagComparatorStrategy());
		this.cosineSimilarityComperator = new TagCloudComparator(new CosineSimilarityTagComparatorStrategy());
	}

	
	
	/**
	 * Writes given dendrogram into neo4j database
	 * 
	 * @param dendrogram
	 * @return
	 * @throws Exception 
	 */
	public void write(Dendrogram<T> dendrogram) throws Exception {
		this.writer.beginTransaction();
		try {
			if (dendrogram.isLeaf()) {
				// dendrogram consists of a single leaf node
				this.createNodeForLeaf((LeafDendrogram<T>)dendrogram);
			} else {
				// dendrogram contains at least to objects
				Node node = this.createNodeForLink((LinkDendrogram<T>)dendrogram);
				this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram1());
				this.writeNode(node, ((LinkDendrogram<T>)dendrogram).dendrogram2());
			}
			this.writer.successTransaction();
		} finally {
			this.writer.finishTransaction();
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
		this.writer.createRelationship(clusterNode, containedNode, RecommenderRelationshipTypes.CONTAINS, null);
	}



	private void createIsMergedIntoRelationship(Node containedNode, Node clusterNode) {
		this.writer.createRelationship(containedNode, clusterNode, RecommenderRelationshipTypes.IS_MERGED_INTO, null);
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
		properties.put("tags", dendrogram.tagCloud().getTagsAsStringArray());
		
		// Write some tag cloud measures
		properties.put(
				"set_difference", 
				this.setDifferenceComperator.compare(
						dendrogram.dendrogram1().tagCloud, 
						dendrogram.dendrogram2().tagCloud
				)
		);
		properties.put(
				"normalized_set_difference", 
				this.normalizedSetDifferenceComperator.compare(
						dendrogram.dendrogram1().tagCloud, 
						dendrogram.dendrogram2().tagCloud
				)
		);
		properties.put(
				"cosine_similarity", 
				this.cosineSimilarityComperator.compare(
						dendrogram.dendrogram1().tagCloud, 
						dendrogram.dendrogram2().tagCloud
				)
		);
		
		return this.writer.createNode(properties);
	}



	private Node createNodeForLeaf(LeafDendrogram<T> dendrogram) {
		RecommenderObject recObject = (RecommenderObject)dendrogram.object();
		de.mknoll.thesis.datastructures.graph.Node node = this.idNodeMap.getNodeByNamespaceAndExternalId(DefaultNamespaces.BIBTIP, recObject.getDocId());
		Node n4jNode = this.writer.getNodeById(Long.parseLong(node.getExternalIdByNamespace(DefaultNamespaces.NEO4J)));
		return n4jNode;
	}
	
}
