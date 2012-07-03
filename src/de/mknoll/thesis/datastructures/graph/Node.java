package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;
import java.util.Map;



/**
 * A node implements a container of information within a graph or a dendrogram.
 * 
 * Further properties can be attached to a node that enable arbitrary information
 * being stored in a node. 
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.NodeTest;
 */
public class Node {

	/**
	 * Holds hashmap of properties attached to this node
	 */
	private HashMap<String, NodeProperty> properties;

	
	
	/**
	 * Holds ID node map for mapping external IDs and corresponding nodes
	 */
	private IdNodeMap idNodeMap;
	
	
	
	/**
	 * Holds internal ID of node
	 */
	private int internalId;



	/**
	 * Holds a map of attached objects
	 */
	private HashMap<String, AttachableToNode> attachedObjects = new HashMap<String, AttachableToNode>();
	
	
	
	/**
	 * Constructor creating default unique id provider
	 */
	public Node(IdNodeMap idNodeMap) {
		this(UniqueNodeIdProvider.getInstance(), idNodeMap);
	}
	
	
	
	/**
	 * Constructor taking node ID provider as argument
	 * 
	 * @param idProvider
	 */
	public Node(NodeIdProvider idProvider, IdNodeMap idNodeMap) {
		this.idNodeMap = idNodeMap;
		this.properties = new HashMap<String, NodeProperty>();
		this.internalId = idProvider.getId();
		this.idNodeMap.createMappingForNode(this);
	}
	
	
	
	/**
	 * Returns internal id of node
	 * 
	 * @return
	 */
	public int internalId() {
		return this.internalId;
	}
	
	
	
	/**
	 * Adds an external ID to node
	 * 
	 * @param namespace
	 * @param externalId
	 */
	public void addExternalId(Namespaces namespace, String externalId) {
		this.idNodeMap.addExternalId(this, namespace, externalId);
	}
	
	
	
	/**
	 * Returns external ID for given namespace
	 * 
	 * @param namespace
	 * @return
	 */
	public String getExternalIdByNamespace(Namespaces namespace) {
		return this.idNodeMap.getExternalId(this, namespace);
	}
	
	
	
	/**
	 * Adds a new property to this node
	 * 
	 * @param propertyName
	 * @param property
	 */
	public void addProperty(String propertyName, NodeProperty property) {
		this.properties.put(propertyName, property);
	}
	
	
	
	/**
	 * Returns a property for a given property name
	 * 
	 * @param propertyName
	 * @return
	 */
	public NodeProperty getProperty(String propertyName) {
		return this.properties.get(propertyName);
	}
	
	
	
	/**
	 * Returns a map of all properties added to this node
	 * 
	 * Map has format
	 * 
	 * Map (
	 * 	 'propertyName' => Map (
	 *     'propertyKey => propertyValue,
	 *     ...
	 *   )
	 * )
	 * 
	 * @return
	 */
	public HashMap<String, HashMap<String, String>> getPropertiesMap() {
		HashMap<String, HashMap<String, String>> properties = new HashMap<String, HashMap<String,String>>();
		for (String propertyName: this.properties.keySet()) {
			properties.put(propertyName, this.properties.get(propertyName).getProperties());
		}
		return properties;
	}
	
	
	
	/**
	 * Returns a flattened property map of attached properties
	 * 
	 * Properties 
	 * 
	 * {
	 * 		outerKey => {
	 *			innerKey1 => prop1,
	 *			innerKey2 => prop2 
	 * 		}
	 * }
	 * 
	 * are flattened to
	 * 
	 * { 
	 * 		OUTERKEY_innerKey1 => prop1,
	 * 		OUTERKEY_innerKey2 => prop2
	 * }
	 * 
	 * @return Flattened property map
	 */
	public Map<String, Object> getFlatPropertiesMap() {
		Map<String, Object> flatPropertiesMap = new HashMap<String, Object>();
		for (String key : this.properties.keySet()) {
			for (String innerKey : this.properties.get(key).getProperties().keySet()) {
				String property = this.properties.get(key).getProperties().get(innerKey);
				
				// Make sure, we do not have null values as those are not allowed as properties in n4j!
				if (property == null) {
					property = "";
				}
				
				flatPropertiesMap.put(key.toUpperCase() + "_" + innerKey, property);
			}
		}
		return flatPropertiesMap;
	}



	public void attachObject(AttachableToNode object) {
		this.attachedObjects.put(object.identifier(), object);
		object.attachTo(this);
	}



	/**
	 * Returns attached object by given identifier
	 * 
	 * @param nodeIdentifier
	 * @return
	 */
	public Object getAttachedObject(String nodeIdentifier) {
		return this.attachedObjects.get(nodeIdentifier);
	}



	/**
	 * Returns true, if external id for given namespace exists on this node
	 * 
	 * @param namespace
	 * @return
	 */
	public boolean hasExternalId(DefaultNamespaces namespace) {
		if (this.idNodeMap.getExternalId(this.internalId, namespace) != null) {
			return true;
		} else {
			return false;
		}
	}
	
}
