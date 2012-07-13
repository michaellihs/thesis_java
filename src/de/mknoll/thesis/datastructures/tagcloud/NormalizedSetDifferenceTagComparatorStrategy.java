package de.mknoll.thesis.datastructures.tagcloud;

import java.util.List;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements tag comparator strategy that calculates
 * a normalized measure of set difference.
 * 
 * Comparison returns 1 if tags are equal and 0 if they are 
 * disjunct.
 * 
 * compare(tags1, tags2) = |tags1 CUT tags2|Ê/ |tags1 UNION tags2|
 * 
 * E.g.
 * 
 * t1 = {tag1, tag2, tag3, tag4}
 * t2 = {            tag3, tag4, tag5, tag6}
 * 
 * compare(t1,t2) = 1/3
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.NormalizedSetDifferenceTagComparatorStrategyTest
 */
public class NormalizedSetDifferenceTagComparatorStrategy extends SetDifferenceTagComparatorStrategy {

	@Override
	public Float compare(List<Tag> tags1, List<Tag> tags2) {
		Float absDifference = super.compare(tags1, tags2);
		Float result;
		if (absDifference == (tags1.size() + tags2.size())) {
			result = 1F;
		} else if (absDifference == 0F) {
			result = 0F;
		} else {
			Float intersectCount = (tags1.size() + tags2.size() - absDifference) / 2;
			result = intersectCount / (tags1.size() + tags2.size() - intersectCount); 
		}
		
		return (1 - result);
	}

}
