/**
 * 
 */
package org.mcavallo.opencloud.test;

import java.util.*;

import org.mcavallo.opencloud.*;
import org.mcavallo.opencloud.filters.*;



import junit.framework.TestCase;

public class TestCloud extends TestCase {

	public void testConstructors() {
		// Default constructor
		Cloud c = new Cloud();
		assertEquals(c.size(), 0);
		
		// Copy constructor
		// TODO test to complete
		c.addTag("pippo","pippo.com");
		c.addTag("paperino","paperino.com");
		c.addTag("pluto","pluto.com");
		assertEquals(c.size(), 3);
		
		Cloud c2 = new Cloud(c);
		assertEquals(c2, c);
	}
	
	public void testAddTag() {
		Cloud c = new Cloud();
		assertEquals(c.size(), 0);
		
		c.addTag("pippo");
		c.addTag("paperino", "paperino.com");
		c.addTag("paperino");
		c.addTag(new Tag("pluto", "pluto.com",3.0));
		c.addTag(new Tag("pluto",null,2.0));
		assertEquals(c.size(), 3);
		
		List<Tag> tagsList = new ArrayList<Tag>();
		tagsList.add(new Tag("qui", "qui.com", new Date(1000)));
		tagsList.add(new Tag("quo", "quo.com"));
		tagsList.add(new Tag("qua", "qua_no.com", new Date(1000)));
		tagsList.add(new Tag("qua", "qua.com", new Date(2000)));
		tagsList.add(new Tag("qua", "qua.com", new Date(500)));
		c.addTags(tagsList);
		assertEquals(c.size(), 6);

		assertEquals(c.getTag("pippo").getLink(),(new Tag().getLink()));
		assertEquals(c.getTag("pippo").getScore(),(new Tag().getScore()));
		assertEquals(c.getTag("paperino").getLink(),"paperino.com");
		assertEquals(c.getTag("paperino").getScore(), 2.0, 0.001);
		assertEquals(c.getTag("pluto").getLink(),"pluto.com");
		assertEquals(c.getTag("pluto").getScore(), 5.0, 0.001);
		assertEquals(c.getTag("qui").getLink(),"qui.com");
		assertEquals(c.getTag("qui").getDate(), new Date(1000));
		assertEquals(c.getTag("quo").getLink(),"quo.com");
		assertEquals(c.getTag("qua").getLink(),"qua.com");
		assertEquals(c.getTag("qua").getDate(), new Date(2000));
	}

	public void testGetTag() {
		Cloud c = new Cloud();
		assertEquals(c.getTag("pippo"), null);
		
		c.addTag("pippo");
		c.addTag("paperino", "paperino.com");
		c.addTag("pluto", "pluto.com");

		assertEquals(c.getTag("qui"), null);
		assertEquals(c.getTag("pippo").getLink(), (new Tag().getLink()));
		assertEquals(c.getTag("paperino").getLink(), "paperino.com");
		assertEquals(c.getTag("pluto").getLink(), "pluto.com");
		assertEquals(c.getTag("pluto").getLink(), "pluto.com");
	}

	public void testRemoveTag() {
		Cloud c = new Cloud();
		c.removeTag("pippo");
		
		c.addTag("pippo");
		c.addTag("paperino", "paperino.com");
		c.addTag("paperino");
		c.addTag("pluto", "pluto.com");
		assertEquals(c.size(), 3);
		
		c.removeTag("paperino");
		assertEquals(c.size(), 2);
		assertEquals(c.getTag("paperino"), null);
		
		c.removeTag("qui");
		assertEquals(c.size(), 2);

		c.removeTag("pippo"); 
		c.removeTag("pluto");
		assertEquals(c.size(), 0);
	}

	public void testTagCase() {
		// Cloud is case insensitive by default
		Cloud c = new Cloud();
		c.addTag("pipPO");
		assertNotNull(c.getTag("PIPpo"));
		c.removeTag("PIPpo");
		assertEquals(0, c.size());

		List<Tag> tags;
		int size;
		
		// Lower case
		c.clear();
		c.setTagCase(Cloud.Case.LOWER);
		c.addTag("pipPO");
		assertEquals("pippo", c.getTag("PIPpo").getName());
		size = c.size();
		c.removeTag("PIPpo");
		assertEquals(size-1, c.size());
		c.addTag("papeRINO");
		c.addTag("pluTO");
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("paperino", tags.get(0).getName());
		assertEquals("pluto", tags.get(1).getName());
		
		// Upper case
		c.clear();
		c.setTagCase(Cloud.Case.UPPER);
		c.addTag("pipPO");
		assertEquals("PIPPO", c.getTag("PIPpo").getName());
		size = c.size();
		c.removeTag("PIPpo");
		assertEquals(size-1, c.size());
		c.addTag("papeRINO");
		c.addTag("pluTO");
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("PAPERINO", tags.get(0).getName());
		assertEquals("PLUTO", tags.get(1).getName());

		// Capitalization
		c.clear();
		c.setTagCase(Cloud.Case.CAPITALIZATION);
		c.addTag("pipPO");
		assertEquals("Pippo", c.getTag("PIPpo").getName());
		size = c.size();
		c.removeTag("PIPpo");
		assertEquals(size-1, c.size());
		c.addTag("papeRINO");
		c.addTag("pluTO");
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("Paperino", tags.get(0).getName());
		assertEquals("Pluto", tags.get(1).getName());

		// Preserve case
		c.clear();
		c.setTagCase(Cloud.Case.PRESERVE_CASE);
		c.addTag("pipPO");
		assertNotNull(c.getTag("PIPpo"));
		assertEquals("pipPO", c.getTag("PIPpo").getName());
		assertEquals(1, c.size());
		c.addTag("PIPpo");
		assertEquals(1, c.size());
		assertNotNull(c.getTag("pipPO"));
		assertEquals("PIPpo", c.getTag("PIPpo").getName());
		size = c.size();
		c.removeTag("PIPpo");
		assertEquals(size-1, c.size());
		c.addTag("papeRINO");
		c.addTag("pluTO");
		c.addTag("PLUto");
		assertEquals(2, c.size());
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("papeRINO", tags.get(0).getName());
		assertEquals("PLUto", tags.get(1).getName());

		// Case sensitive
		c.clear();
		c.setTagCase(Cloud.Case.CASE_SENSITIVE);
		c.addTag("pipPO");
		assertNull(c.getTag("PIPpo"));
		assertEquals("pipPO", c.getTag("pipPO").getName());
		size = c.size();
		c.removeTag("PIPpo");
		assertEquals(size, c.size());
		c.removeTag("pipPO");
		assertEquals(size-1, c.size());
		c.addTag("papeRINO");
		c.addTag("pluTO");
		c.addTag("PLUto");
		assertEquals(3, c.size());
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("papeRINO", tags.get(0).getName());
		assertEquals("PLUto", tags.get(1).getName());
		assertEquals("pluTO", tags.get(2).getName());
		
		// Change between case lower/upper/capitalization case settings
		// shouldn't cause any problem
		c.clear();
		c.setTagCase(Cloud.Case.LOWER);
		c.addTag("papeRINO");
		c.addTag("pluTO");
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("paperino", tags.get(0).getName());
		assertEquals("pluto", tags.get(1).getName());
		c.setTagCase(Cloud.Case.UPPER);
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("PAPERINO", tags.get(0).getName());
		assertEquals("PLUTO", tags.get(1).getName());
		c.setTagCase(Cloud.Case.CAPITALIZATION);
		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals("Paperino", tags.get(0).getName());
		assertEquals("Pluto", tags.get(1).getName());
	}

	public void testClear() {
		Cloud c = new Cloud();
		c.clear();
		assertEquals(c.size(), 0);
		
		c.addTag("pippo");
		c.addTag("paperino", "paperino.com");
		c.addTag("paperino");
		c.addTag("pluto", "pluto.com");
		assertEquals(c.size(), 3);
		
		c.clear();
		assertEquals(c.size(), 0);
	}

	public void testIsValid() {
		assertFalse(Cloud.isValid(new Tag((String)null)));
		assertFalse(Cloud.isValid(new Tag(null, null)));
		assertFalse(Cloud.isValid(new Tag("")));
		
		assertTrue(Cloud.isValid(new Tag("pippo",null)));
		assertTrue(Cloud.isValid(new Tag("pippo",null,null)));
		assertTrue(Cloud.isValid(new Tag("pippo",null,1.0)));
		assertTrue(Cloud.isValid(new Tag("pippo","",1.0)));
		assertTrue(Cloud.isValid(new Tag("pippo","pippo.com",1.0)));
		assertTrue(Cloud.isValid(new Tag("pippo",null,1.0,null)));
		
		assertFalse(Cloud.isValid(new Tag("pippo",null,-1.0)));
		assertFalse(Cloud.isValid(new Tag("pippo",null,0.0)));
		assertFalse(Cloud.isValid(new Tag("pippo",null,Double.NaN)));
		assertFalse(Cloud.isValid(new Tag("pippo",null,Double.NEGATIVE_INFINITY)));
		assertFalse(Cloud.isValid(new Tag("pippo",null,Double.POSITIVE_INFINITY)));
		assertTrue(Cloud.isValid(new Tag("pippo",null,Double.MAX_VALUE)));
		assertTrue(Cloud.isValid(new Tag("pippo",null,Double.MIN_VALUE)));
	}

	public void testAddText() {
		Cloud c = new Cloud();
		
		c.addText("-, _, n, n--o, 01, n@, y2, 3s�, 4y, -no5, _no6, no7-, n�8_, yes-yes9");
		assertEquals(9, c.size());
		assertEquals(c.getTag("01").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("y2").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("3s�").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("4y").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("no5").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("no6").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("no7").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("n�8").getLink(), c.getDefaultLink());
		assertEquals(c.getTag("yes-yes9").getLink(), c.getDefaultLink());

		c.clear();
		assertEquals(c.size(), 0);
		c.addText("pippo, paperino, pluto","http://www.%s.com");
		assertEquals(c.size(), 3);
		assertEquals(c.getTag("pippo").getLink(),"http://www.pippo.com");
		assertEquals(c.getTag("paperino").getLink(),"http://www.paperino.com");
		assertEquals(c.getTag("pluto").getLink(),"http://www.pluto.com");
	}

	public void testInputFilters() {
		Cloud c = new Cloud();
		Filter<Tag> filter1 = new AcceptAllFilter<Tag>();
		Filter<Tag> filter2 = new AcceptNoneFilter<Tag>();
		
		assertEquals(c.getInputFilters().size(), 0);
		c.addInputFilter(filter1);
		assertEquals(c.getInputFilters().size(), 1);
		c.addTag("pippo");
		c.addTag("paperino");
		assertEquals(c.size(), 2);

		c.addInputFilter(filter2);
		assertEquals(c.getInputFilters().size(), 2);
		c.addTag("qui");
		c.addTag("quo");
		assertEquals(c.size(), 2);
		
		c.removeInputFilter(filter2);
		assertEquals(c.getInputFilters().size(), 1);
		c.addTag("qui");
		c.addTag("quo");
		assertEquals(c.size(), 4);
		
		c.clearInputFilters();
		assertEquals(0, c.getInputFilters().size());
		
		c.addInputFilter(new AcceptAllFilter<Tag>());
		c.addInputFilter(new AcceptAllFilter<Tag>());
		c.removeInputFilters(AcceptAllFilter.class);
		assertEquals(0, c.getInputFilters().size());
	}

	public void testOuputFilters() {
		Cloud c = new Cloud();
		Filter<Tag> filter1 = new AcceptAllFilter<Tag>();
		Filter<Tag> filter2 = new AcceptNoneFilter<Tag>();
		
		assertEquals(c.getOutputFilters().size(), 0);
		c.addOutputFilter(filter1);
		assertEquals(c.getOutputFilters().size(), 1);
		c.addTag("pippo");
		c.addTag("paperino");
		assertEquals(c.size(), 2);
		assertEquals(c.tags().size(), 2);

		c.addOutputFilter(filter2);
		assertEquals(c.getOutputFilters().size(), 2);
		c.addTag("qui");
		c.addTag("quo");
		assertEquals(c.size(), 4);
		assertEquals(c.tags().size(), 0);
		
		c.removeOutputFilter(filter2);
		assertEquals(c.getOutputFilters().size(), 1);
		assertEquals(c.tags().size(), 4);
		
		c.clearOutputFilters();
		assertEquals(c.getOutputFilters().size(), 0);
		assertEquals(c.tags().size(), 4);
		
		c.addOutputFilter(new AcceptAllFilter<Tag>());
		c.addOutputFilter(new AcceptAllFilter<Tag>());
		c.removeOutputFilters(AcceptAllFilter.class);
		assertEquals(0, c.getOutputFilters().size());
	}

	public void testGetTags() {
		Cloud c = new Cloud();
		List<Tag> tags;
		assertEquals(c.tags().size(), 0);
		
		c.addTag(new Tag("a",null,4.0));
		c.addTag(new Tag("b",null,5.0));
		c.addTag(new Tag("c",null,6.0));
		c.addTag(new Tag("d",null,1.0));
		c.addTag(new Tag("e",null,2.0));
		c.addTag(new Tag("f",null,3.0));
		c.setMaxWeight(4);		

		// check default ordering
		assertEquals(c.tags(new Tag.NameComparatorAsc()), c.tags());

		tags = c.tags(new Tag.ScoreComparatorDesc());
		assertEquals(tags.size(), 6);
		assertEquals(tags.get(0).getName(), "c");
		assertEquals(tags.get(0).getScore(), 6.0, 0.001);
		assertEquals(tags.get(0).getNormScore(), 1.0, 0.001);
		assertEquals(tags.get(0).getWeightInt(), 4);
		assertEquals(tags.get(tags.size()-1).getName(), "d");
		assertEquals(tags.get(tags.size()-1).getScore(), 1.0, 0.001);
		assertEquals(tags.get(tags.size()-1).getNormScore(), 0.167, 0.001);
		assertEquals(tags.get(tags.size()-1).getWeightInt(), 1);

		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals(tags.size(), 6);
		assertEquals(tags.get(0).getName(), "a");
		assertEquals(tags.get(0).getScore(), 4.0, 0.001);
		assertEquals(tags.get(0).getNormScore(), 0.667, 0.001);
		assertEquals(tags.get(0).getWeightInt(), 3);
		assertEquals(tags.get(tags.size()-1).getName(), "f");
		assertEquals(tags.get(tags.size()-1).getScore(), 3.0, 0.001);
		assertEquals(tags.get(tags.size()-1).getNormScore(), 0.5, 0.001);
		assertEquals(tags.get(tags.size()-1).getWeightInt(), 2);

		c.setMaxWeight(2);
		tags = c.tags(new Tag.ScoreComparatorDesc());
		assertEquals(tags.size(), 6);
		assertEquals(tags.get(0).getName(), "c");
		assertEquals(tags.get(0).getScore(), 6.0, 0.001);
		assertEquals(tags.get(0).getNormScore(), 1.0, 0.001);
		assertEquals(tags.get(0).getWeightInt(), 2);
		assertEquals(tags.get(tags.size()-1).getName(), "d");
		assertEquals(tags.get(tags.size()-1).getScore(), 1.0, 0.001);
		assertEquals(tags.get(tags.size()-1).getNormScore(), 0.167, 0.001);
		assertEquals(tags.get(tags.size()-1).getWeightInt(), 1);

		c.setMaxTagsToDisplay(4);
		tags = c.tags(new Tag.ScoreComparatorDesc());
		assertEquals(tags.size(), 4);
		assertEquals(tags.get(0).getName(), "c");
		assertEquals(tags.get(0).getScore(), 6.0, 0.001);
		assertEquals(tags.get(0).getNormScore(), 1.0, 0.001);
		assertEquals(tags.get(0).getWeightInt(), 2);
		assertEquals(tags.get(tags.size()-1).getName(), "f");
		assertEquals(tags.get(tags.size()-1).getScore(), 3.0, 0.001);
		assertEquals(tags.get(tags.size()-1).getNormScore(), 0.5, 0.001);
		assertEquals(tags.get(tags.size()-1).getWeightInt(), 1);

		tags = c.tags(new Tag.NameComparatorAsc());
		assertEquals(tags.size(), 4);
		assertEquals(tags.get(0).getName(), "a");
		assertEquals(tags.get(0).getScore(), 4.0, 0.001);
		assertEquals(tags.get(0).getNormScore(), 0.667, 0.001);
		assertEquals(tags.get(0).getWeightInt(), 2);
		assertEquals(tags.get(tags.size()-1).getName(), "f");
		assertEquals(tags.get(tags.size()-1).getScore(), 3.0, 0.001);
		assertEquals(tags.get(tags.size()-1).getNormScore(), 0.5, 0.001);
		assertEquals(tags.get(tags.size()-1).getWeightInt(), 1);
	}

	public void testGetAllTags() {
		Cloud c = new Cloud();
		List<Tag> tags,tagsToAdd;
		c.setMaxWeight(4);
		c.setMaxTagsToDisplay(4);

		
		// Tests on empty cloud
		assertEquals(0, c.allTags().size());
		
		// Adds a collection of tags to the cloud
		tagsToAdd = new ArrayList<Tag>();
		tagsToAdd.add(new Tag("a",null,4.0));
		tagsToAdd.add(new Tag("b",null,5.0));
		tagsToAdd.add(new Tag("c",null,6.0));
		tagsToAdd.add(new Tag("d",null,1.0));
		tagsToAdd.add(new Tag("e",null,2.0));
		tagsToAdd.add(new Tag("f",null,3.0));
		c.addTags(tagsToAdd);

		// Retrieves all the tags
		tags = c.allTags();
		
		// Sorts tags alphabetically
		Collections.sort(tags, new Tag.NameComparatorAsc());
		
		// Checks that the two collection are equal
		assertEquals(tagsToAdd, tags);
	}

}
