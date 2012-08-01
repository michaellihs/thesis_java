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
	public Double compare(List<Tag> tags1, List<Tag> tags2) {
		Double absDifference = super.compare(tags1, tags2);
		Double result;
		if (absDifference == (tags1.size() + tags2.size())) {  // Both tag-lists are totally disjunct --> return 0
//			System.out.println("Disjunct case: absDifference = " + absDifference + " tags1.size = " + tags1.size() + " tags2.size = " + tags2.size());
//			this.print(tags1);
//			this.print(tags2);
//			System.out.println();
			result = 0D;
		} else if (absDifference == 0D) {   // Both tag-lists are similar --> return 1
//			System.out.println("Equivalent case: absDifference = " + absDifference);
			result = 1D;
//			this.print(tags1);
//			this.print(tags2);
//			System.out.println();
		} else {
			Double intersectCount = (tags1.size() + tags2.size() - absDifference) / 2;
			result = intersectCount / (tags1.size() + tags2.size() - intersectCount); 
		}
		
		return result;
		// return (1 - result);
	}
	
	
	
	private void print(List<Tag> tags) {
		for (Tag tag : tags) {
			System.out.print("\"" + tag.getName() + "\", ");
		}
		System.out.println("\n");
	}

}
