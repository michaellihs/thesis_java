package de.mknoll.thesis.datastructures.graph;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Class implements a mapping for IDs
 * 
 * A node can have several ids. There is only one internal id which is
 * directly accessible via node object. All other ids are stored in this mapping.
 * Each external id must have a namespace. Namespaces need to be set 
 * when creating a IdNodeMap object and can't be changed or extended later.
 * Therefore a enumeration of namespaces is given to the constructor.
 * 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.IdNodeMapTest
 */
public class DefaultIdNodeMap implements IdNodeMap {

	/**
	 * Holds a mapping node(internalId)->namespace->externalId
	 */
	ArrayList<HashMap<Namespaces, String>> indexMapping = new ArrayList<HashMap<Namespaces, String>>();
	
	
	
	/**
	 * Holds a mapping namespace->externalId->node
	 */
	HashMap<Namespaces, HashMap<String, Node>> reverseMapping = new HashMap<Namespaces, HashMap<String, Node>>();
	
	
	
	/**
	 * Holds a mapping internalId --> node
	 */
	HashMap<Integer, Node> internalIdToNodeMapping = new HashMap<Integer, Node>();

	
	
	public DefaultIdNodeMap() {
		this(DefaultNamespaces.class);
	}
	
	
	
	/**
	 * Constructor takes a set of namespaces and creates mapping entries for all of them.
	 * 
	 * Namespaces have to be part of a enumeration which is passed here as parameter.
	 * 
	 * @param namespaces
	 */
	public <T extends Namespaces> DefaultIdNodeMap(Class<T> namespaces) {
		for (Namespaces namespace : namespaces.getEnumConstants()) {
			this.reverseMapping.put(namespace, new HashMap<String, Node>());
		}
	}
	
	

	/**
	 * Adds an external ID to the index mapping
	 * 
	 * @param node Node to add external id for
	 * @param namespace Namespace of id
	 * @param externalId Id to be added
	 * @Override 
	 */
	public void addExternalId(Node node, Namespaces namespace, String externalId) {
		HashMap<Namespaces, String> nodeIdsFromIndexMapping =  this.getNodeIdsFromIndexMapping(node);
		nodeIdsFromIndexMapping.put(namespace, externalId);
		this.internalIdToNodeMapping.put(node.internalId(), node);
		this.reverseMapping.get(namespace).put(externalId, node);
	}
	
	

	/**
	 * Adds a new node to index mapping
	 * 
	 * This should be called from constructor of node, whenever a new node is created.
	 * 
	 * @param node
	 */
	public void createMappingForNode(Node node) {
		this.addNodeToIndexMapping(node);
	}
	
	
	
	public String getExternalId(Node node, Namespaces namespace) {
		return getExternalId(node.internalId(), namespace);
	}
	
	
	
	public String getExternalId(int internalId, Namespaces namepsace) {
		return this.getNodeIdsFromIndexMapping(internalId).get(namepsace);
	}
	
	
	
	public Node getNodeByNamespaceAndExternalId(Namespaces namespace, String externalId) {
		return this.reverseMapping.get(namespace).get(externalId);
	}



	public Node getNodeByInternalId(Integer internalId) {
		return this.internalIdToNodeMapping.get(internalId);
	}
	
	
	
	public Integer getInternalIdByNamespaceAndExternalId(Namespaces namespace, String externalId) {
		return this.getNodeByNamespaceAndExternalId(namespace, externalId).internalId();
	}
	
	
	
	private void addNodeToIndexMapping(Node node) {
		// if we want to add element on arbitrary index, we have to fill all elements before with null (if not yet filled)
		if (this.indexMapping.size() < (node.internalId() - 1)) {
			for (int i = 0; i < node.internalId(); i++) {
				if (this.indexMapping.size() <= i) {
					this.indexMapping.add(i, null);
				}
			}
		}
		this.indexMapping.add(node.internalId() - 1, new HashMap<Namespaces, String>());
		this.internalIdToNodeMapping.put(node.internalId(), node);
	}
	
	
	
	private HashMap<Namespaces, String> getNodeIdsFromIndexMapping(Node node) {
		return this.getNodeIdsFromIndexMapping(node.internalId());
	}
	
	
	
	private HashMap<Namespaces, String> getNodeIdsFromIndexMapping(Integer internalId) {
		return this.indexMapping.get(internalId - 1);
	}
	
}
