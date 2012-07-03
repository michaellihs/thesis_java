package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import de.mknoll.thesis.tests.datastructures.dendrogram.LinkDendrogramTest;



/**
 * Class implements a link within a dendrogram
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class LinkDendrogram<T> extends Dendrogram<T> {

	private Dendrogram<T> dendrogram1;
	private Dendrogram<T> dendrogram2;
	
	
	
	public LinkDendrogram(Dendrogram<T> dendrogram1, Dendrogram<T> dendrogram2) {
		this.dendrogram1 = dendrogram1;
		this.dendrogram2 = dendrogram2;
		
		dendrogram1.setParent(this);
		dendrogram2.setParent(this);
	}
	
	
	
	public LinkDendrogram(Dendrogram<T> dendrogram1, Dendrogram<T> dendrogram2, String id) {
		this(dendrogram1, dendrogram2);
		this.id = id;
	}
	
	
	
	public Set<T> memberSet() {
		HashSet<T> set = new HashSet<T>();
		this.addMembers(set);
		return set;
	}
	
	
	
	public void addMembers(Set<T> set) {
		this.dendrogram1.addMembers(set);
		this.dendrogram2.addMembers(set);
	}
	
	
	
	public Dendrogram<T> dendrogram1() {
		return this.dendrogram1;
	}
	
	
	
	public Dendrogram<T> dendrogram2() {
		return this.dendrogram2;
	}



	@Override
	void split(HashSet<Dendrogram<T>> resultSet, LinkedList<Dendrogram<T>> queue) {
		queue.add(this.dendrogram1());
		queue.add(this.dendrogram2());
	}



	@Override
	public boolean isLeaf() {
		return false;
	}
	
}
