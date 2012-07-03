package de.mknoll.thesis.datastructures.graph;



/**
 * Interface for node-to-id maps
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface IdNodeMap {
	
	public void addExternalId(Node node, Namespaces namespace, String externalId);
	
	
	
	public void createMappingForNode(Node node);
	
	
	
	public String getExternalId(Node node, Namespaces namespace);
	
	
	
	public String getExternalId(int internalId, Namespaces namepsace);
	
	
	
	public Node getNodeByNamespaceAndExternalId(Namespaces namespace, String externalId);
	
	
	
	public Integer getInternalIdByNamespaceAndExternalId(Namespaces namespace, String externalId);

}
