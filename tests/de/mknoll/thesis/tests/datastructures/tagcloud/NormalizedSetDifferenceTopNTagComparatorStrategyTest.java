package de.mknoll.thesis.tests.datastructures.tagcloud;

import java.util.ArrayList;
import java.util.List;

import junit.framework.*;
import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategy;
import de.mknoll.thesis.tests.AllTests;



/**
 * Class implements test for set-difference top-n tag comparator.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategy
 */
public class NormalizedSetDifferenceTopNTagComparatorStrategyTest {

	@Test
	public void compareReturnsExpectedValueIfOne() {
		Tag tag1_1 = new Tag("tag1", 4.0);
		Tag tag1_2 = new Tag("tag2", 3.0);
		Tag tag1_3 = new Tag("tag3", 2.0);
		Tag tag1_4 = new Tag("tag4", 1.0);
		List<Tag> tags1 = new ArrayList<Tag>(); tags1.add(tag1_1); tags1.add(tag1_2); tags1.add(tag1_3); tags1.add(tag1_4);
		
		Tag tag2_1 = new Tag("tag1", 4.0);
		Tag tag2_2 = new Tag("tag2", 3.0);
		Tag tag2_3 = new Tag("tag5", 2.0);
		Tag tag2_4 = new Tag("tag6", 1.0);
		List<Tag> tags2 = new ArrayList<Tag>(); tags2.add(tag2_1); tags2.add(tag2_2); tags2.add(tag2_3); tags2.add(tag2_4);
		
		NormalizedSetDifferenceTopNTagComparatorStrategy comparator = new NormalizedSetDifferenceTopNTagComparatorStrategy(2);
		Double result = comparator.compare(tags1, tags2);
		
		Assert.assertTrue((Math.abs(result - 1.0) < AllTests.EPSILON));
	}
	
	
	
	@Test
	public void compareReturnsExpectedValueIfZero() {
		Tag tag1_1 = new Tag("tag5", 4.0);
		Tag tag1_2 = new Tag("tag6", 3.0);
		Tag tag1_3 = new Tag("tag3", 2.0);
		Tag tag1_4 = new Tag("tag4", 1.0);
		List<Tag> tags1 = new ArrayList<Tag>(); tags1.add(tag1_1); tags1.add(tag1_2); tags1.add(tag1_3); tags1.add(tag1_4);
		
		Tag tag2_1 = new Tag("tag1", 4.0);
		Tag tag2_2 = new Tag("tag2", 3.0);
		Tag tag2_3 = new Tag("tag3", 2.0);
		Tag tag2_4 = new Tag("tag4", 1.0);
		List<Tag> tags2 = new ArrayList<Tag>(); tags2.add(tag2_1); tags2.add(tag2_2); tags2.add(tag2_3); tags2.add(tag2_4);
		
		NormalizedSetDifferenceTopNTagComparatorStrategy comparator = new NormalizedSetDifferenceTopNTagComparatorStrategy(2);
		Double result = comparator.compare(tags1, tags2);
		
		Assert.assertTrue((Math.abs(result - 0.0) < AllTests.EPSILON));
	}
	
	
	
	@Test
	public void compareReturnsExpectedValue() {
		Tag tag1_1 = new Tag("tag1", 4.0);
		Tag tag1_2 = new Tag("tag5", 3.0);
		Tag tag1_3 = new Tag("tag3", 2.0);
		Tag tag1_4 = new Tag("tag4", 1.0);
		List<Tag> tags1 = new ArrayList<Tag>(); tags1.add(tag1_1); tags1.add(tag1_2); tags1.add(tag1_3); tags1.add(tag1_4);
		
		Tag tag2_1 = new Tag("tag1", 4.0);
		Tag tag2_2 = new Tag("tag2", 3.0);
		Tag tag2_3 = new Tag("tag3", 2.0);
		Tag tag2_4 = new Tag("tag4", 1.0);
		List<Tag> tags2 = new ArrayList<Tag>(); tags2.add(tag2_1); tags2.add(tag2_2); tags2.add(tag2_3); tags2.add(tag2_4);
		
		NormalizedSetDifferenceTopNTagComparatorStrategy comparator = new NormalizedSetDifferenceTopNTagComparatorStrategy(2);
		Double result = comparator.compare(tags1, tags2);
		
		System.out.println(result);
		
		Assert.assertTrue((Math.abs(result - (2.0/3.0)) < AllTests.EPSILON));
	}
	
	
}
