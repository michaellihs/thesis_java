package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy;




/**
 * Class implements a testcase for normalized set tag comparator
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategy
 */
public class NormalizedSetDifferenceTagComparatorStrategyTest {
	
	/**
	 * Holds comparator class to be tested here
	 */
	private NormalizedSetDifferenceTagComparatorStrategy comparator = new NormalizedSetDifferenceTagComparatorStrategy();
	
	
	
	@Test
	public void compareReturnsZeroIfTagCloudsAreDisjunct() {
		Cloud c1 = new Cloud();
		c1.addTag("tag1");
		
		Cloud c2 = new Cloud();
		c2.addTag("tag2");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 0F);
	}
	
	
	
	@Test
	public void compareReturnsOneIfCloudsAreEqual() {
		Cloud c1 = new Cloud();
		c1.addTag("tag1");
		c1.addTag("tag2");
		
		Cloud c2 = new Cloud();
		c2.addTag("tag1");
		c2.addTag("tag2");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 1F);
	}
	
	
	@Test
	public void compareReturnsExpectedValue() {
		Cloud c1 = new Cloud();
		c1.addTag("tag1");
		c1.addTag("tag2");
		c1.addTag("tag3");
		
		Cloud c2 = new Cloud();
		c2.addTag("tag2");
		c2.addTag("tag3");
		c2.addTag("tag5");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 0.5F);
	}

}
