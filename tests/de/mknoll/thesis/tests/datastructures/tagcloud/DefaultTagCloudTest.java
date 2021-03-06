package de.mknoll.thesis.tests.datastructures.tagcloud;

import junit.framework.Assert;

import org.junit.Test;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;



/**
 * Class implements a test for a default tag cloud
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud
 */
public class DefaultTagCloudTest extends AbstractTagCloudTest {

	@Test
	public void expectedTagsCanBeAdded() {
		DefaultTagCloud cloud = new DefaultTagCloud();
		Tag tag0 = new Tag("1"); // should be filtered due to min length
		Tag tag1 = new Tag("ein"); // should be filtered due to stopword
		Tag tag2 = new Tag("Analysis"); // should not be filtered
		cloud.addTag(tag0);
		cloud.addTag(tag1);
		cloud.addTag(tag2);
		
		Assert.assertTrue(cloud.tags().contains(tag2));
		Assert.assertFalse(cloud.tags().contains(tag0));
		Assert.assertFalse(cloud.tags().contains(tag1));
	}
	
	
	
	@Test
	public void tagCloudsAreMergedAsExpected() throws Exception {
		
		/**
		 *  Testing, what's happening inside link dendrogram for getting two tag clouds merged
		 */
		
		DefaultTagCloud cloud1 = new DefaultTagCloud("22", "52008", "anleitungen", "atmen", "aufbereitung", "ausstellung", "bilderdatenbank", "boden", "bodenweltende", "deutsches", "elementarbildung", "elemente", "entdecken", "entsorgung", "erde", "experimente", "feuer", "forscher", "forscherlabor", "freiarbeit", "gase", "gasen", "gegriffen", "geht", "gemisch", "grundschule", "haus", "herkunft", "infolinos", "infosystem", "internetplattform", "kinder", "kindergarten", "luft", "natur", "online", "thema", "unterrichtseinheit", "wasser", "zzzebra");
		DefaultTagCloud cloud2 = new DefaultTagCloud("alltags", "gel�st", "maus", "r�tsel", "sachgeschichten");
		DefaultTagCloud mergedCloud = new DefaultTagCloud(cloud1);
		mergedCloud.addTags(cloud2.allTags());
		
		this.assertContainsAllTags(mergedCloud, "22", "52008", "anleitungen", "atmen", "aufbereitung", "ausstellung", "bilderdatenbank", "boden", "bodenweltende", "deutsches", "elementarbildung", "elemente", "entdecken", "entsorgung", "erde", "experimente", "feuer", "forscher", "forscherlabor", "freiarbeit", "gase", "gasen", "gegriffen", "geht", "gemisch", "grundschule", "haus", "herkunft", "infolinos", "infosystem", "internetplattform", "kinder", "kindergarten", "luft", "natur", "online", "thema", "unterrichtseinheit", "wasser", "zzzebra", "alltags", "gel�st", "maus", "r�tsel", "sachgeschichten");
	}
	
}