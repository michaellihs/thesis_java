package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements tag comparator strategy that calculates
 * cardinality of set difference of given tag sets.
 * 
 * Comparison returns 0 if both tag sets are equal and a maximum
 * of |tags1| + |tags2| if they are disjunct.
 * 
 * E.g.
 * 
 * t1 = {tag1, tag2, tag3}
 * t2 = {      tag2, tag3, tag4}
 * 
 * compare(t1,t2) = 2
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class SetDifferenceTagComparatorStrategy implements TagComparatorStrategy {

	/**
	 * Holds value added to difference if tag is in one cloud but not in the other.
	 */
	protected Double diffCount = 1.0;
	
	
	
	/**
	 * Binary merge?
	 * 
	 * @Override
	 */
	public Double compare(List<Tag> tags1, List<Tag> tags2) {
		// Make tag lists sorted by ascending tag name
		Collections.sort(tags1, new Tag.NameComparatorAsc());
		Collections.sort(tags2, new Tag.NameComparatorAsc());
		
		// Assertion: tags are sorted in alphabetical order
		
		Double difference = 0D;
		Iterator<Tag> it1 = tags1.iterator();
		Iterator<Tag> it2 = tags2.iterator();
		Tag t1;
		Tag t2;
		if (it1.hasNext()) t1 = it1.next(); else t1 = null; 
		if (it2.hasNext()) t2 = it2.next(); else t2 = null;
		while (t1 != null || t2 != null) {
			if (t1 == null) {
				difference += this.diffCount;
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			} else if (t2 == null) {
				difference += this.diffCount;
				if (it1.hasNext()) t1 = it1.next(); else t1 = null;
			} else if (t1.equals(t2)) {
				if (it1.hasNext()) t1 = it1.next(); else t1 = null; 
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			} else if (t1.getName().compareToIgnoreCase(t2.getName()) < 0) {
				difference += this.diffCount;
				if (it1.hasNext()) t1 = it1.next(); else t1 = null;
			} else {
				difference += this.diffCount;
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			}
		}
		return difference;
	}

}
