package de.mknoll.thesis.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.mknoll.thesis.tests.algorithms.GermanStemmerTest;
import de.mknoll.thesis.tests.analysis.ModularityAnalyzerTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.JsonDendrogramWriterTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.LeafDendrogramTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.LinkDendrogramTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.Neo4jDendrogramWriterTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.NewmanJoinsDendrogramReaderTest;
import de.mknoll.thesis.tests.datastructures.dendrogram.RecommenderObjectDendrogramBuilderTest;
import de.mknoll.thesis.tests.datastructures.graph.IdNodeMapTest;
import de.mknoll.thesis.tests.datastructures.graph.NodeTest;
import de.mknoll.thesis.tests.datastructures.graph.RecommenderObjectTest;
import de.mknoll.thesis.tests.datastructures.graph.writer.MetisWriterTest;
import de.mknoll.thesis.tests.datastructures.graph.writer.Neo4jWriterTest;
import de.mknoll.thesis.tests.di.DependencyInjectionTest;
import de.mknoll.thesis.tests.framework.core.BootstrapTest;
import de.mknoll.thesis.tests.framework.database.PostgresConnectionTest;
import de.mknoll.thesis.tests.datastructures.graph.RecommendationGraphTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.CloudTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.CosineSimilarityTagComparatorStrategyTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.DefaultTagCloudTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.DendrogramTagCloudComparatorTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategyTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategyTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.SetDifferenceTagComparatorStrategyTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.StopWordFilteredTagCloudTest;
import de.mknoll.thesis.tests.datastructures.tagcloud.TagExtractorTest;
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
	JsonDendrogramWriterTest.class,
	Neo4jTest.class,
	Neo4jWriterTest.class,
	Neo4jDendrogramWriterTest.class,
	NodeTest.class,
	IdNodeMapTest.class,
	MetisWriterTest.class,
	TagExtractorTest.class,
	DefaultTagCloudTest.class,
	RecommenderObjectTest.class,
	SetDifferenceTagComparatorStrategyTest.class,
	NormalizedSetDifferenceTagComparatorStrategyTest.class,
	CosineSimilarityTagComparatorStrategyTest.class,
	ModularityAnalyzerTest.class,
	NormalizedSetDifferenceTopNTagComparatorStrategyTest.class,
	StopWordFilteredTagCloudTest.class,
	GermanStemmerTest.class,
	CloudTest.class,
	DendrogramTagCloudComparatorTest.class
} )



/**
 * Class implements setting for all junit tests in this thesis.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class AllTests {

	/**
	 * Holds constant for equality comparison of doubles
	 */
	public static final double EPSILON = 0.0000000000001;

}
