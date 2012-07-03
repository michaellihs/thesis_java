package de.mknoll.thesis.tests.neo4j;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.graphdb.Node;


/**
 * Class implements some tests for usage of neo4j graph database.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class Neo4jTest {

	@Test
	public void connectionToServerCanBeEstablished() {
		String uri = "http://localhost:7474/db/data/";
		Map<String, Object> nodeProperties = new HashMap<String, Object>();
		nodeProperties.put("testkey", "testvalue");
		RestAPI rApi = new RestAPI(uri);
		rApi.createNode(nodeProperties);
		/*
		RestGraphDatabase restGraphDatabase = new RestGraphDatabase(uri);
		Node node = restGraphDatabase.createNode();
		Assert.assertEquals(node, restGraphDatabase.getNodeById(node.getId()));
		*/
	}
	
}
