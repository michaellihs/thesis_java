package de.mknoll.thesis.tests.datastructures.tagcloud;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.CosineSimilarityTagComparatorStrategy;



/**
 * Class implements testcase for cosine similarity tag comparator
 * 
 * @author Michale Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.CosineSimilarityTagComparatorStrategy
 */
public class CosineSimilarityTagComparatorStrategyTest {

	/**
	 * Holds instance of class to be tested
	 */
	private CosineSimilarityTagComparatorStrategy comparator = new CosineSimilarityTagComparatorStrategy();
	
	
	
	/**
	 * Some epsilon for equality in floats
	 */
	private static final Double EPSILON = 0.0000001D;
	
	
	
	@Test
	public void compareThrowsExceptionOnEmptyTagSets() {
		List<Tag> tags1 = new ArrayList<Tag>();
		List<Tag> tags2 = new ArrayList<Tag>();
		Assert.assertTrue(this.comparator.compare(tags1, tags2) == 0);
	}
	
	
	
	@Test
	public void compareReturnsOneOnEqualVectors() {
		List<Tag> tags1 = new ArrayList<Tag>();
		List<Tag> tags2 = new ArrayList<Tag>();
		tags1.add(new Tag("tag1"));
		tags1.add(new Tag("tag2"));
		tags2.add(new Tag("tag1"));
		tags2.add(new Tag("tag2"));
		Assert.assertTrue(Math.abs(this.comparator.compare(tags1, tags2) - 1) < EPSILON);
	}
	
	
	
	@Test
	public void compareReturnsZeroOnDisjunctVectors() {
		List<Tag> tags1 = new ArrayList<Tag>();
		List<Tag> tags2 = new ArrayList<Tag>();
		tags1.add(new Tag("tag1"));
		tags1.add(new Tag("tag2"));
		tags2.add(new Tag("tag3"));
		tags2.add(new Tag("tag4"));
		Assert.assertTrue(this.comparator.compare(tags1, tags2) == 0);
	}
	
	
	
	@Test
	public void compareReturnsExpectedValue() {
		Tag tag1_2 = new Tag("tag1");
		Tag tag1_1 = new Tag("tag1");
		Tag tag2_2 = new Tag("tag2");
		Tag tag3_4 = new Tag("tag3");
		Tag tag4_4 = new Tag("tag4");
		Tag tag4_2 = new Tag("tag4");
		tag1_2.setScore(2);
		tag1_1.setScore(1);
		tag2_2.setScore(2);
		tag3_4.setScore(4);
		tag4_4.setScore(4);
		tag4_2.setScore(2);
		
		List<Tag> tags1 = new ArrayList<Tag>();
		tags1.add(tag1_2);
		tags1.add(tag2_2);
		tags1.add(tag4_4);
		
		List<Tag> tags2 = new ArrayList<Tag>();
		tags2.add(tag1_1); 
		tags2.add(tag3_4); 
		tags2.add(tag4_2);
		
		Assert.assertTrue(Math.abs(this.comparator.compare(tags1, tags2) - (10 / (Math.sqrt(24) * Math.sqrt(21)))) < EPSILON);
	}
	
}
