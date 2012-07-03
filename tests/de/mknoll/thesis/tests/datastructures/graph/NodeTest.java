package de.mknoll.thesis.tests.datastructures.graph;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.DefaultNamespaces;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.NodeIdProvider;
import de.mknoll.thesis.datastructures.graph.NodeProperty;
import de.mknoll.thesis.datastructures.graph.UniqueNodeIdProvider;


/**
 * Class implements testcase for generic graph nodes.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class NodeTest {

	@Test
	public void nodeCanBeConstructed() {
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		@SuppressWarnings("unused")
		Node node = new Node(idNodeMapping);
	}
	
	
	
	@Test
	public void nodePropertyCanBeAddedToNode() {
		Node node = this.createNodeWithMappingAndUniqueIdProvider();
		NodeProperty nodeProperty = new NodePropertyMock();
		node.addProperty("firstProperty", nodeProperty);
	}
	
	
	
	@Test 
	public void getPropertiesReturnsPropertiesOfAddedNodeProperties() {
		HashMap<String, String> property1 = new HashMap<String, String>();
		HashMap<String, String> property2 = new HashMap<String, String>();
		NodePropertyMock nodeProperty1 = new NodePropertyMock(property1);
		NodePropertyMock nodeProperty2 = new NodePropertyMock(property2);
		
		Node node = this.createNodeWithMappingAndUniqueIdProvider();
		
		node.addProperty("nodeProperty1", nodeProperty1);
		node.addProperty("nodeProperty2", nodeProperty2);
		
		HashMap<String, HashMap<String, String>> properties = node.getPropertiesMap();
		Assert.assertTrue(properties.containsKey("nodeProperty1"));
		Assert.assertTrue(properties.containsKey("nodeProperty2"));
		Assert.assertTrue(properties.get("nodeProperty1").equals(property1));
		Assert.assertTrue(properties.get("nodeProperty2").equals(property2));
	}
	
	
	
	@Test
	public void idCreatedByNodeIdProviderIsReturnedIfSetInConstructor() {
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(new UniqueNodeIdProviderMock(), idNodeMapping);
		Assert.assertEquals(10, node.internalId());
	}
	
	
	
	@Test
	public void seriesOfIdIsCreatedWhenUsingDefaultIdProvider() {
		// Make sure, node id starts with expected value
		UniqueNodeIdProvider idProvider = UniqueNodeIdProvider.getInstance();
		idProvider.reset();
		int firstId = idProvider.firstId();
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		
		Node node1 = new Node(idNodeMapping);
		Node node2 = new Node(idNodeMapping);
		Node node3 = new Node(idNodeMapping);
		Assert.assertEquals(node1.internalId(), firstId);
		Assert.assertEquals(node2.internalId(), firstId + 1);
		Assert.assertEquals(node3.internalId(), firstId + 2);
	}
	
	
	
	@Test
	public void addingExternalIdsToNodeAddsIdsToNodeMapping() {
		UniqueNodeIdProvider.getInstance().reset();
		String externalId = "ol:1234";
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(idNodeMapping);
		node.addExternalId(DefaultNamespaces.BIBTIP, externalId);
		Assert.assertTrue(idNodeMapping.getExternalId(node.internalId(), DefaultNamespaces.BIBTIP).equals(externalId));
	}
	
	
	
	@Test
	public void getFlatPropertiesMapReturnsExpectedMap() {
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(idNodeMapping);
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("innerKey1", "value1");
		properties.put("innerKey2", "value2");
		node.addProperty("key", new NodePropertyMock(properties));
		
		Map<String, Object> flattenedProperties = node.getFlatPropertiesMap();
		
		Assert.assertTrue(flattenedProperties.get("KEY_innerKey1").equals("value1"));
		Assert.assertTrue(flattenedProperties.get("KEY_innerKey2").equals("value2"));
	}
	
	
	
	@Test
	public void hasExternalIdReturnsTrueOnExistingExternalId() {
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(idNodeMapping);
		node.addExternalId(DefaultNamespaces.BIBTIP, "ol:1");
		Assert.assertTrue(node.hasExternalId(DefaultNamespaces.BIBTIP));
	}
	
	
	
	@Test
	public void hasExternalIdReturnsFalseOnNonExistingExternalId() {
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(idNodeMapping);
		Assert.assertFalse(node.hasExternalId(DefaultNamespaces.BIBTIP));
	}
	
	
	
	private Node createNodeWithMappingAndUniqueIdProvider() {
		UniqueNodeIdProvider.getInstance().reset();
		IdNodeMap idNodeMapping = new DefaultIdNodeMap();
		Node node = new Node(UniqueNodeIdProvider.getInstance(), idNodeMapping);
		return node;
	}
	
	
	
	/**
	 * Class implements NodeProperty mock used for testing only
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class NodePropertyMock implements NodeProperty {
		
		private HashMap<String, String> properties;


		
		public NodePropertyMock() {
			this.properties = new HashMap<String, String>();
		}
		
		
		
		public NodePropertyMock(HashMap<String, String> properties) {
			this.properties = properties;
		}
		
		

		@Override
		public HashMap<String, String> getProperties() {
			return this.properties;
		}
		
	}
	
	
	
	private class UniqueNodeIdProviderMock implements NodeIdProvider {
		
		public int getId() {
			return 10;
		}
		
	}
	
}
