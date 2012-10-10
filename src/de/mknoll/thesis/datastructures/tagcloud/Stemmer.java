package de.mknoll.thesis.datastructures.tagcloud;



/**
 * Interface for classes that implement a stemmer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface Stemmer {

	public String stem(String term);
	
}
