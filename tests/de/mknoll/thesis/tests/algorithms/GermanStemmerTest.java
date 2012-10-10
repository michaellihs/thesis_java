package de.mknoll.thesis.tests.algorithms;

import org.apache.lucene.analysis.de.GermanStemmer;
import org.junit.Test;

import junit.framework.Assert;



/**
 * Class implements testcase for German stemmer
 * 
 * @see org.apache.lucene.analysis.de.GermanStemmer
 * @author mimi
 *
 */
public class GermanStemmerTest {

	@Test
	public void stemReturnsExpectedResult() {
		GermanStemmer stemmer = new GermanStemmer();
		String stemmedTerm = stemmer.stem("gehen");
		Assert.assertTrue(stemmedTerm.equals("geh"));
	}
	
}
