package de.mknoll.thesis.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.mknoll.thesis.tests.datastructures.dendrogram.JsonDendrogramWriterTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.LeafDendrogramTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.LinkDendrogramTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.Neo4jDendrogramWriterTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.NewmanJoinsDendrogramReaderTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.RecommenderObjectDendrogramBuilderTest;
import de.mknoll.thesis.tests.datastructures.graph.IdNodeMapTest;
import de.mknoll.thesis.tests.datastructures.graph.IdentifierRecommenderObjectMapTest;
import de.mknoll.thesis.tests.datastructures.graph.NodeTest;
import de.mknoll.thesis.tests.datastructures.graph.UniqueIdProviderTest;
import de.mknoll.thesis.tests.datastructures.graph.writer.MetisWriterTest;
import de.mknoll.thesis.tests.datastructures.graph.writer.Neo4jWriterTest;
import de.mknoll.thesis.tests.di.DependencyInjectionTest;
import de.mknoll.thesis.tests.framework.core.BootstrapTest;
import de.mknoll.thesis.tests.framework.database.PostgresConnectionTest;
import de.mknoll.thesis.tests.datastructures.graph.RecommendationGraphTest;
import de.mknoll.thesis.tests.neo4j.Neo4jTest;



/**
 * Testsuite for all tests within thesis test package.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
	BootstrapTest.class, 
	RecommendationGraphTest.class, 
	PostgresConnectionTest.class, 
	DependencyInjectionTest.class, 
	LeafDendrogramTest.class, 
	LinkDendrogramTest.class, 
	RecommenderObjectDendrogramBuilderTest.class,
	NewmanJoinsDendrogramReaderTest.class,
	IdentifierRecommenderObjectMapTest.class,
	UniqueIdProviderTest.class,
	JsonDendrogramWriterTest.class,
	Neo4jTest.class,
	Neo4jWriterTest.class,
	Neo4jDendrogramWriterTest.class,
	NodeTest.class,
	IdNodeMapTest.class,
	MetisWriterTest.class
} )

public class AllTests {

}
