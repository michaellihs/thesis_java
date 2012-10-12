package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.Stemmer;
import de.mknoll.thesis.datastructures.tagcloud.TagStemMap;



/**
 * Class implements a testcase for tag-stem-map
 * 
 * @author Michael Knoll
 * @see de.mknoll.thesis.datastructures.tagcloud.TagStemMap
 */
public class TagStemMapTest {

	@Test
	public void tagCanBeInserted() {
		Tag tag = new Tag("string");
		Stemmer stemmer = new DummyStemmer();
		TagStemMap map = new TagStemMap(stemmer);
		map.put(tag);
	}
	
	
	
	@Test
	public void getStemReturnsExpectedStemForGivenTag() {
		Tag tag = new Tag("string");
		Stemmer stemmer = new DummyStemmer();
		TagStemMap map = new TagStemMap(stemmer);
		map.put(tag);
		Assert.assertTrue("dummy".equals(map.getStemForTag(tag)));
	}
	
	
	
	@Test
	public void getMostImportantTagReturnsExpectedTagForGivenStem() {
		Tag tag = new Tag("string");
		Stemmer stemmer = new ReverseStemmer();
		TagStemMap map = new TagStemMap(stemmer);
		map.put(tag);
		Assert.assertTrue(map.getMostImportantTagForStem("gnirts").equals(tag));
	}
	
	
	
	@Test
	public void getMostImportantTagReturnsExpectedTagIfMoreThanOneTagAdded() {
		Tag tag1 = new Tag("string");
		Tag tag2 = new Tag("string");
		Stemmer stemmer = new ReverseStemmer();
		TagStemMap map = new TagStemMap(stemmer);
		map.put(tag1);
		map.put(tag2);
		map.put(tag1);
		Assert.assertTrue(map.getMostImportantTagForStem("gnirts").equals(tag1));
		
		map.put(tag2);
		map.put(tag2);
		Assert.assertTrue(map.getMostImportantTagForStem("gnirts").equals(tag2));
	}
	
	
	
	/**
	 * Class implements a dummy stemmer for testing
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class DummyStemmer implements Stemmer {

		/**
		 * Dummy stemmer that returns "dummy" for every given term
		 */
		public String stem(String term) {
			return "dummy";
		}
		
	}
	
	
	
	/**
	 * Class implements dummy stemmer for testing
	 * 
	 * @author Michael Knoll <mimi@kaktusteam.de>
	 */
	private class ReverseStemmer implements Stemmer {

		/**
		 * Dummy stemmer that returns reversed term
		 */
		public String stem(String term) {
			return new StringBuffer(term).reverse().toString();
		}
		
	}
	
}