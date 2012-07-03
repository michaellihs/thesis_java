package de.mknoll.thesis.tests.datastructures.graph;

import junit.framework.Assert;

import org.junit.Test;

import de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap;
import de.mknoll.thesis.datastructures.graph.IdNodeMap;
import de.mknoll.thesis.datastructures.graph.Namespaces;
import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.UniqueNodeIdProvider;



/**
 * Class implements a testcase for id-node-mapping
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.graph.DefaultIdNodeMap
 */
public class IdNodeMapTest {

	@Test
	public void constructorTakesNamespacesEnumClassAsArgument() {
		/**
		 * Enum with namespaces needs to be given to id node map
		 * at construction time. Can't be changed later.
		 */
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
	}
	
	
	
	@Test
	public void testCreateMappingForNode() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
	}

	
	
	@Test
	public void testAddExternalId() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
		map.addExternalId(node, NamespacesMock.BIBTIP, "ol:1234");
	}
	
	

	@Test
	public void testGetExternalIdNodeString() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
		map.addExternalId(node, NamespacesMock.BIBTIP, "ol:1234");
		
		Assert.assertTrue(map.getExternalId(node, NamespacesMock.BIBTIP).equals("ol:1234"));
	}
	
	

	@Test
	public void testGetExternalIdIntegerString() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
		map.addExternalId(node, NamespacesMock.BIBTIP, "ol:1234");
		
		Assert.assertTrue(map.getExternalId(node.internalId(), NamespacesMock.BIBTIP).equals("ol:1234"));
	}
	
	

	@Test
	public void testGetNodeByNamespaceAndExternalId() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
		map.addExternalId(node, NamespacesMock.BIBTIP, "ol:1234");
		
		Assert.assertTrue(map.getNodeByNamespaceAndExternalId(NamespacesMock.BIBTIP, "ol:1234").equals(node));
	}
	
	

	@Test
	public void testGetInternalIdByNamespaceAndExternalId() {
		UniqueNodeIdProvider.getInstance().reset();
		
		DefaultIdNodeMap map = new DefaultIdNodeMap(NamespacesMock.class);
		Node node = new Node(map);
		map.addExternalId(node, NamespacesMock.BIBTIP, "ol:1234");
		
		Assert.assertTrue(map.getInternalIdByNamespaceAndExternalId(NamespacesMock.BIBTIP, "ol:1234").equals(node.internalId()));
	}
	
	
	
	@Test
	public void nodeMappingCanBeInsertedForArbitraryNodeIds() {
		// we have a node mock, that internal id = 10
		IdNodeMap map = new DefaultIdNodeMap();
		Node nodeMock = new NodeMock(map);
	}
	
	
	
	/**
	 * Enumeration of dummy namespaces for this testcase
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private enum NamespacesMock implements Namespaces {
		NEO4J, BIBTIP
	}
	
	
	
	/**
	 * Class implements a node mock that always returns 10 as internal id
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class NodeMock extends Node {

		public NodeMock(IdNodeMap idNodeMap) {
			super(idNodeMap);
			// TODO Auto-generated constructor stub
		}
		
		
		public int internalId() {
			return 10;
		}
	}

}
