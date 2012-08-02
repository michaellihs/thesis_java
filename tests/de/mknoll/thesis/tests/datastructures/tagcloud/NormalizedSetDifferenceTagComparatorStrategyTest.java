package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
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
		c1.addTag("tag3");
		c1.addTag("tag4");
		
		Cloud c2 = new Cloud();
		c2.addTag("tag2");
		c2.addTag("tag6");
		c2.addTag("tag7");
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
	
	
	
	@Test
	public void compareReturnsExpectedValue2() {
		Cloud c1 = new Cloud();
		c1.addTag("1");
		c1.addTag("2");
		c1.addTag("3");
		c1.addTag("4");
		
		Cloud c2 = new Cloud();
		c2.addTag("1");
		c2.addTag("2");
		c2.addTag("3");
		c2.addTag("5");
		Double similarity = this.comparator.compare(c1.allTags(), c2.allTags());
		System.out.println(similarity);
		Assert.assertTrue(similarity == (3.0/5.0));
	}
	
	
	
	@Test
	public void compareReturnsExpectedValueOnBigTagsets() {
		DefaultTagCloud c1 = new DefaultTagCloud();
		c1.addTags("allgemein", "als", "arbeit", "auf", "aus", "bachelor", "badenwürttemberg", "bereich", "berufliche", "bildung", "datenbanken", "den", "des", "deutsch", "deutsche", "deutschen", "deutschland", "durch", "eb", "erwachsenenbildung", "erwachsenenbildungsrecht", "ev", "forschung", "frauen", "für", "handbuch", "heft", "hochschulen", "hochschullehre", "im", "in", "lernen", "linkempfehlungen", "mit", "nach", "nordrheinwestfalen", "online", "projekte", "soziale", "sozialen", "sprache", "studiengänge", "studierende", "studium", "von", "weiterbildung", "zeitschrift", "zu", "zum", "zur");

		DefaultTagCloud c2 = new DefaultTagCloud();
		c2.addTags("aufstiegsstipendium", "begabte", "begabtenförderung", "berufstätige", "bestimmungen", "bildungssponsoring", "förderalmanach", "förderprogramme", "für", "gesetz", "handbuch", "hochschulbereich", "im", "in", "individuelle", "nationalen", "nrwstipendienprogramm", "rechtliche", "schaffung", "schule", "schulsponsoring", "sponsoring", "stipendienprogrammgesetz", "stipendienprogramms", "stipg", "studienförderung", "studienstiftungen", "werbung", "zum", "zur"); 
		
		Double similarity = this.comparator.compare(c1.allTags(), c2.allTags()); 
		System.out.println(similarity);
		Assert.assertTrue(similarity == (3.0/37));
	}
	
	
	
	@Test
	public void compareReturnsExpectedValue3() {
		DefaultTagCloud c1 = new DefaultTagCloud();
		c1.addTags("2012", "and", "bildung", "bildungsserver", "chemie", "conference", "deutschen", "deutschland", "education", "entwicklung", "fachtagung", "förderung", "geschichte", "grundschule", "heft", "informationskompetenz", "international", "internationale", "kinder", "konferenz", "learning", "lernen", "linktipps", "materialien", "neue", "pädagogik", "schule", "schulen", "thema", "unterricht", "unterrichtsanregung", "unterrichtseinheit", "unterrichtsmaterial", "zeitschrift");
		
		DefaultTagCloud c2 = new DefaultTagCloud();
		c2.addTags("1949", "1961", "19611989", "20", "50", "alltag", "arbeitsblätter", "aufarbeitung", "bau", "berliner", "beziehungen", "bildungsangebote", "chronik", "ddr", "ddrdiktatur", "ddrgeschichte", "defafilm", "demokratiebewusstsein", "deutschdeutsche", "deutsche", "diktatur", "drei", "einheit", "fwu", "handreichungen", "heile", "herrschaft", "informationen", "jahre", "materialien", "mauer", "mauerbau", "offlinemedien", "perspektiven", "plakatausstellung", "politische", "sedherrschaft", "sodis", "stasi", "stärken", "themenfeld", "thüringen", "unterricht", "unterrichtseinheit");
		
		Double similarity = this.comparator.compare(c1.allTags(), c2.allTags()); 
		System.out.println(similarity);
	}
	
	
	
	@Test
	public void compareReturnsExpectedValue4() {
		DefaultTagCloud c1 = new DefaultTagCloud();
		c1.addTags("2012", "and", "bildung", "bildungsserver", "chemie", "conference", "deutschen", "deutschland", "education", "entwicklung", "fachtagung", "förderung", "geschichte", "grundschule", "heft", "informationen", "international", "internationale", "kinder", "konferenz", "learning", "lernen", "linktipps", "materialien", "neue", "pädagogik", "schule", "schulen", "thema", "unterricht", "unterrichtsanregung", "unterrichtseinheit", "unterrichtsmaterial", "zeitschrift"); 

		DefaultTagCloud c2 = new DefaultTagCloud();
		c2.addTags("510", "afrika", "afrikas", "aids", "analysen", "bekanntem", "bevölkerungsentwicklung", "bildung", "bücher", "ca", "deutschland", "fremdem", "grundschulalter", "grundschulunterricht", "jahre", "kartografixschule", "kinder", "kinderalltag", "kinderarbeit", "kindergarten", "land", "länder", "nil", "projekt", "regionen", "sag", "suche", "südafrika", "teil", "umbruch", "unterrichtsanregung", "unterrichtseinheit", "unterrichtsprojekt", "vergleich", "video", "ägypten");
		
		Double similarity = this.comparator.compare(c1.allTags(), c2.allTags()); 
		System.out.println(similarity);
	}

}
