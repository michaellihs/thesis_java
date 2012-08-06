package de.mknoll.thesis.tests.datastructures.tagcloud;

import org.junit.Test;

import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;



/**
 * Class implements testcase for stopword-filtered tag cloud implementation
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.datastructures.tagcloud.StopWordFilteredTagCloud
 */
public class StopWordFilteredTagCloudTest extends AbstractTagCloudTest {
	
	@Test
	public void tagCloudsAreMergedAsExpected() throws Exception {
		
		/**
		 *  Testing, what's happening inside link dendrogram for getting two tag clouds merged
		 */
		
		DefaultTagCloud cloud1 = new DefaultTagCloud("22", "52008", "anleitungen", "atmen", "aufbereitung", "ausstellung", "bilderdatenbank", "boden", "bodenweltende", "deutsches", "elementarbildung", "elemente", "entdecken", "entsorgung", "erde", "experimente", "feuer", "forscher", "forscherlabor", "freiarbeit", "gase", "gasen", "gegriffen", "geht", "gemisch", "grundschule", "haus", "herkunft", "infolinos", "infosystem", "internetplattform", "kinder", "kindergarten", "luft", "natur", "online", "thema", "unterrichtseinheit", "wasser", "zzzebra");
		DefaultTagCloud cloud2 = new DefaultTagCloud("alltags", "gelöst", "maus", "rätsel", "sachgeschichten");
		DefaultTagCloud mergedCloud = new DefaultTagCloud(cloud1);
		mergedCloud.addTags(cloud2.allTags());
		
		this.assertContainsAllTags(mergedCloud, "22", "52008", "anleitungen", "atmen", "aufbereitung", "ausstellung", "bilderdatenbank", "boden", "bodenweltende", "deutsches", "elementarbildung", "elemente", "entdecken", "entsorgung", "erde", "experimente", "feuer", "forscher", "forscherlabor", "freiarbeit", "gase", "gasen", "gegriffen", "geht", "gemisch", "grundschule", "haus", "herkunft", "infolinos", "infosystem", "internetplattform", "kinder", "kindergarten", "luft", "natur", "online", "thema", "unterrichtseinheit", "wasser", "zzzebra", "alltags", "gelöst", "maus", "rätsel", "sachgeschichten");
	}

}