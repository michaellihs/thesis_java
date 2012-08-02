package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Collections;
import java.util.List;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements tag comparator strategy that calculates
 * a normalized measure of set difference for two tagclouds.
 * 
 * For the first tag-cloud, only top-n tags are taken, and compared
 * to second tag-cloud.
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
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.NormalizedSetDifferenceTopNTagComparatorStrategyTest
 */
public class NormalizedSetDifferenceTopNTagComparatorStrategy extends NormalizedSetDifferenceTagComparatorStrategy {
	
	/**
	 * Holds number of top-tags to compare
	 */
	int n;
	
	
	
	/**
	 * Constructor takes number of tags to compare
	 * 
	 * @param n Number of tags to compare
	 */
	public NormalizedSetDifferenceTopNTagComparatorStrategy(int n) {
		this.n = n;
	}
	
	

	/**
	 * Compares top-n tags of both given tag lists
	 * 
	 * @return Normalized set difference of both given tag lists
	 */
	public Double compare(List<Tag> tags1, List<Tag> tags2) {
		
		// TODO how to handle n?
		// 1. Fixed n
		// 2. n calculated by #tags in given lists
		
		// make sure, we don't have bigger n than elements in tags list
		if (tags1.size() == 0 || tags2.size() == 0) return 0D; // assertion: |tags1| > 0 AND |tags2| > 0
		if (tags1.size() <= this.n) this.n = tags1.size() - 1;
		if (tags2.size() <= this.n) this.n = tags2.size() - 1;
		
		Collections.sort(tags1, new Tag.ScoreComparatorDesc());
		List<Tag> topN_1 = tags1.subList(0, this.n);
		
		Collections.sort(tags2, new Tag.ScoreComparatorDesc());
		List<Tag> topN_2 = tags2.subList(0, this.n);
		
		return super.compare(topN_1, topN_2);
		
		
//		Double absDifference = super.compare(tags1, tags2);
//		Double result;
//		if (absDifference == (tags1.size() + tags2.size())) {
//			result = 1D;
//		} else if (absDifference == 0D) {
//			result = 0D;
//		} else {
//			Double intersectCount = (tags1.size() + tags2.size() - absDifference) / 2;
//			result = intersectCount / (tags1.size() + tags2.size() - intersectCount); 
//		}
//		
//		return (1 - result);
	}

}
