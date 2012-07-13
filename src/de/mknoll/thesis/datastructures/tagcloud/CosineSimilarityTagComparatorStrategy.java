package de.mknoll.thesis.datastructures.tagcloud;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.mcavallo.opencloud.Tag;



/**
 * Class implements tag comparator strategy using cosine similarity
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.tagcloud.CosineSimilarityTagComparatorStrategyTest
 */
public class CosineSimilarityTagComparatorStrategy implements TagComparatorStrategy {
	
	/**
	 * Holds weigthed tag vectors
	 * 
	 * E.g. for input 
	 * t1 = {tag1=10,         tag3=5, tag4=3        }
	 * t2 = {         tag2=7,         tag4=5, tag5=3}
	 * 
	 * we get tag vectors
	 * tagVector1 = {10,0,5,3,0}
	 * tagVector2 = { 0,7,0,5,3}
	 */
	private Vector<Integer> tagVector1;
	private Vector<Integer> tagVector2;
	
	

	@Override
	public Double compare(List<Tag> tags1, List<Tag> tags2) {
		// If both vectors are empty, we return 0
		if ((tags1.size() == 0) && (tags2.size() == 0)) {
			return 0D;
		}
		
		// Make tag lists sorted by ascending tag name
		Collections.sort(tags1, new Tag.NameComparatorAsc());
		Collections.sort(tags2, new Tag.NameComparatorAsc());
		
		this.createTagVectors(tags1, tags2);
		Double result = this.calculateCosineSimilarity();
		
		return result;
	}



	private Double calculateCosineSimilarity() {
		// Make sure, we have vectors of equal sizes
		Assert.assertTrue(this.tagVector1.size() == this.tagVector2.size());
		Double fracTop = 0D;
		Double fracBot1 = 0D;
		Double fracBot2 = 0D;
		for (int i = 0; i < this.tagVector1.size(); i++) {
			fracTop += this.tagVector1.get(i) * this.tagVector2.get(i);
			fracBot1 += Math.pow(this.tagVector1.get(i), 2);
			fracBot2 +=	Math.pow(this.tagVector2.get(i), 2);
		}
		return fracTop / (Math.sqrt(fracBot1) * Math.sqrt(fracBot2));
	}



	private void createTagVectors(List<Tag> tags1, List<Tag> tags2) {
		this.tagVector1 = new Vector<Integer>();
		this.tagVector2 = new Vector<Integer>();
		Iterator<Tag> it1 = tags1.iterator();
		Iterator<Tag> it2 = tags2.iterator();
		Tag t1;
		Tag t2;
		if (it1.hasNext()) t1 = it1.next(); else t1 = null; 
		if (it2.hasNext()) t2 = it2.next(); else t2 = null;
		while (t1 != null || t2 != null) {
			if (t1 == null) {
				this.tagVector1.add(0);
				this.tagVector2.add(t2.getScoreInt());
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			} else if (t2 == null) {
				this.tagVector1.add(t1.getScoreInt());
				this.tagVector2.add(0);
				if (it1.hasNext()) t1 = it1.next(); else t1 = null;
			} else if (t1.equals(t2)) {
				this.tagVector1.add(t1.getScoreInt());
				this.tagVector2.add(t2.getScoreInt());
				if (it1.hasNext()) t1 = it1.next(); else t1 = null; 
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			} else if (t1.getName().compareToIgnoreCase(t2.getName()) < 0) {
				this.tagVector1.add(t1.getScoreInt());
				this.tagVector2.add(0);
				if (it1.hasNext()) t1 = it1.next(); else t1 = null;
			} else {
				this.tagVector1.add(0);
				this.tagVector2.add(t2.getScoreInt());
				if (it2.hasNext()) t2 = it2.next(); else t2 = null;
			}
		}
	}

}
