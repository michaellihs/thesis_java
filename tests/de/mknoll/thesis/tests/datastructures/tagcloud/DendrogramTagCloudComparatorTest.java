package de.mknoll.thesis.tests.datastructures.tagcloud;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.DendrogramTagCloudComparator;
import de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudComparator;
import de.mknoll.thesis.datastructures.tagcloud.TagComparatorStrategy;



/**
 * Class implements testcase for dendrogram tag cloud comparator
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.DendrogramTagCloudComparator
 */
public class DendrogramTagCloudComparatorTest {

	@Test
	public void dendrogramToBeComparedCanBeSetInConstructor() {
		DendrogramTagCloudComparator comparator = new DendrogramTagCloudComparator(new LeafDendrogram<RecommenderObject>(new RecommenderObject()));
	}
	
	
	
	@Test
	public void addComparatorAllowsSettingOfTagCouldComparator() {
		DendrogramTagCloudComparator comparator = new DendrogramTagCloudComparator(new LeafDendrogram<RecommenderObject>(new RecommenderObject()));
		comparator.addComparator("SetDifference", new TagCloudComparator(new SetDifferenceTagComparatorStrategy()));
	}
	
	
	
	@Test
	public void expectedSetDifferenceIsWrittenToAdditionalValuesForGivenDendrograms() {
		RecommenderObject recObj1 = new RecommenderObject("tag1 tag2 tag3 tag4");
		RecommenderObject recObj2 = new RecommenderObject("tag1 tag3 tag5 tag6");
		LeafDendrogram<RecommenderObject> leaf1 = new LeafDendrogram<RecommenderObject>(recObj1);
		LeafDendrogram<RecommenderObject> leaf2 = new LeafDendrogram<RecommenderObject>(recObj2);
		LinkDendrogram<RecommenderObject> link = new LinkDendrogram<RecommenderObject>(leaf1, leaf2);
		
		String comparatorName = "setDifference";
		DendrogramTagCloudComparator comparator = new DendrogramTagCloudComparator(link);
		
		TagComparatorStrategy comparatorStrategyMock = new setDifferenceComparatorStrategyMock();
		
		comparator.addComparator(comparatorName, new TagCloudComparator(comparatorStrategyMock));
		
		comparator.runComparison();
		
		// Comparator should set additional values for leaf1 and leaf 2 since they have a parent node
		Assert.assertEquals(leaf1.getAdditionalValue(comparatorName), 10.0);
		Assert.assertEquals(leaf2.getAdditionalValue(comparatorName), 10.0);
		
		// There should be no value set for link, since link has no parent in this dendrogram
		Assert.assertEquals(link.getAdditionalValue(comparatorName), null);
	}
	
	

	/**
	 * Mock class for testing
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class setDifferenceComparatorStrategyMock implements TagComparatorStrategy {

		@Override
		public Double compare(List<Tag> tags1, List<Tag> tags2) {
			return 10.0;
		}
		
	}
	
}
