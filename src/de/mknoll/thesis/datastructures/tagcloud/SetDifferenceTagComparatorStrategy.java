package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Iterator;
import java.util.List;

import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.Tag.NameComparatorAsc;



/**
 * Class implements tag comparator strategy that calculates
 * cardinality of set difference of given tag sets.
 * 
 * E.g.
 * 
 * t1 = {tag1, tag2, tag3}
 * t2 = {      tag2, tag3, tag4}
 * 
 * compare(t1,t2) = 2
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.SetDifferenceTagComparatorStrategyTest
 */
public class SetDifferenceTagComparatorStrategy implements TagComparatorStrategy {

	@Override
	public Float compare(List<Tag> tags1, List<Tag> tags2) {
		// Assertion: tags are sorted in alphabetical order
		Float difference = 0F;
		Iterator<Tag> it1 = tags1.iterator();
		Iterator<Tag> it2 = tags2.iterator();
		Tag t1;
		Tag t2;
		while (it1.hasNext() && it2.hasNext()) {
			t1 = it1.next();
			t2 = it2.next();
			if (t1.equals(t2)) {
				t1 = it1.next();
				t2 = it2.next();
			} else if (t1.getName().compareToIgnoreCase(t2.getName()) < 0) {
				difference += 1;
				t1 = it1.next();
			} else {
				difference += 1;
				t2 = it2.next();
			}
		}
		while (it1.hasNext()) {
			it1.next();
			difference += 1;
		}
		while (it2.hasNext()) {
			it2.next();
			difference += 1;
		}
		return difference;
	}

}
