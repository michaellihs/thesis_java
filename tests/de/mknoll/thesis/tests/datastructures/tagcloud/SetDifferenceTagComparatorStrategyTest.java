package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy;



/**
 * Class implements testcase for set difference tag comparator strategy.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.SetDifferenceTagComparatorStrategy
 */
public class SetDifferenceTagComparatorStrategyTest {
	
	/**
	 * Holds instance of class to be tested
	 */
	private SetDifferenceTagComparatorStrategy comparator = new SetDifferenceTagComparatorStrategy();
	
	
	
	@Test
	public void compareReturnsZeroForEmptyTagSets() {
		Cloud c1 = new Cloud();
		Cloud c2 = new Cloud();
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 0F);
	}
	
	
	
	@Test
	public void compareReturnsTagSizeIfOneTagSetIsEmpty() {
		Cloud c1 = new Cloud();
		Cloud c2 = new Cloud();
		c1.addTag("tag1");
		c1.addTag("tag2");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 2F);
		
		c1 = new Cloud();
		c2 = new Cloud();
		c2.addTag("tag1");
		c2.addTag("tag2");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 2F);
	}
	
	
	
	@Test
	public void compareReturnsExpectedDifference() {
		Cloud c1 = new Cloud();
		Cloud c2 = new Cloud();
		c1.addTag("tag2");
		c1.addTag("tag1");
		c2.addTag("tag3");
		c2.addTag("tag4");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 4F);
	}
	
	
	
	@Test
	public void compareReturnsZeroOnEqualClouds() {
		Cloud c1 = new Cloud();
		c1.addTag("tag2");
		c1.addTag("tag1");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c1.allTags()) == 0F);
	}
	
	
	
	@Test
	public void compareReturnsExpectedValueOnDisjunctTagsets() {
		Cloud c1 = new Cloud();
		c1.addTag("tag2");
		c1.addTag("tag1");
		Cloud c2 = new Cloud();
		c2.addTag("tag3");
		c2.addTag("tag4");
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 4F);
	}
	
	
	
	@Test
	public void compareReturnsExpectedValueOnBigTagsets() {
		DefaultTagCloud c1 = new DefaultTagCloud();
		c1.addTags("allgemein", "als", "arbeit", "auf", "aus", "bachelor", "badenwürttemberg", "bereich", "berufliche", "bildung", "datenbanken", "den", "des", "deutsch", "deutsche", "deutschen", "deutschland", "durch", "eb", "erwachsenenbildung", "erwachsenenbildungsrecht", "ev", "forschung", "frauen", "für", "handbuch", "heft", "hochschulen", "hochschullehre", "im", "in", "lernen", "linkempfehlungen", "mit", "nach", "nordrheinwestfalen", "online", "projekte", "soziale", "sozialen", "sprache", "studiengänge", "studierende", "studium", "von", "weiterbildung", "zeitschrift", "zu", "zum", "zur");

		DefaultTagCloud c2 = new DefaultTagCloud();
		c2.addTags("aufstiegsstipendium", "begabte", "begabtenförderung", "berufstätige", "bestimmungen", "bildungssponsoring", "förderalmanach", "förderprogramme", "für", "gesetz", "handbuch", "hochschulbereich", "im", "in", "individuelle", "nationalen", "nrwstipendienprogramm", "rechtliche", "schaffung", "schule", "schulsponsoring", "sponsoring", "stipendienprogrammgesetz", "stipendienprogramms", "stipg", "studienförderung", "studienstiftungen", "werbung", "zum", "zur"); 
		

		System.out.println(c1.allTags().size());
		System.out.println(c2.allTags().size());
		System.out.println(this.comparator.compare(c1.allTags(), c2.allTags()));
		Assert.assertTrue(this.comparator.compare(c1.allTags(), c2.allTags()) == 68F);
	}
	
}
