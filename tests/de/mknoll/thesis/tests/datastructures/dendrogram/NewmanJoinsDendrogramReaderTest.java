package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;



/**
 * Testcase for NewmanJoinsDendrogramReader
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class NewmanJoinsDendrogramReaderTest {
	
	@Test
	public void patternMatchesJoinsFileLine() {
		Pattern joinsFilePattern = Pattern.compile("(\\d+)\\s+(\\d+)");
		Matcher matcher = joinsFilePattern.matcher("12345 67890 12.23 23123");
		
		matcher.find();
//		System.out.println("\"" + matcher.group(1) + "\"");
//		System.out.println("\"" + matcher.group(2) + "\"");
//		System.out.println(matcher.group(1).equals("12345"));
		assertTrue(matcher.group(1).equals("12345"));
		assertTrue(matcher.group(2).equals("67890"));
	}
	
	
	@Test
	public void patternMatchesMinusOneInLine() {
		String line = "-1      -1      -0.00172033     0";
		assertTrue(line.startsWith("-1"));
	}
	
}
