package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.graph.AttachableToNode;
import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;



/**
 * Class implements a link within a dendrogram
 * 
 * A link is an inner node of a dendrogram acting as a merge of two connected children.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.dendrogram.LinkDendrogramTest
 */
public class LinkDendrogram<T extends TagCloudContainer & AttachableToNode> extends Dendrogram<T> {

	/**
	 * First child of this node
	 */
	private Dendrogram<T> dendrogram1;
	
	
	
	/**
	 * Second child of this node
	 */
	private Dendrogram<T> dendrogram2;
	
	
	
	/**
	 * Constructor for an inner node taking two dendrograms to be merged within this node
	 * 
	 * @param dendrogram1
	 * @param dendrogram2
	 */
	public LinkDendrogram(Dendrogram<T> dendrogram1, Dendrogram<T> dendrogram2) {
		this.dendrogram1 = dendrogram1;
		this.dendrogram2 = dendrogram2;
		
		dendrogram1.setParent(this);
		dendrogram2.setParent(this);
	}
	
	
	
	/**
	 * Constructor for an inner node taking an id to be set for this node
	 * 
	 * @param dendrogram1
	 * @param dendrogram2
	 * @param id
	 */
	public LinkDendrogram(Dendrogram<T> dendrogram1, Dendrogram<T> dendrogram2, String id) {
		this(dendrogram1, dendrogram2);
		this.id = id;
	}
	
	
	
	/**
	 * Template method for getting leaf set of dendrogram
	 * 
	 * @return Leaves of dendrogram
	 */
	public Set<LeafDendrogram<T>> leaves() {
		HashSet<LeafDendrogram<T>> leaves = new HashSet<LeafDendrogram<T>>();
		this.addLeaves(leaves);
		return leaves;
	}
	
	
	
	/**
	 * Returns a set of all members (leaves) of this dendrogram
	 */
	public Set<T> memberSet() {
		HashSet<T> set = new HashSet<T>();
		this.addMembers(set);
		return set;
	}
	
	
	
	/**
	 * Adds members of this dendrogram to given set of members.
	 */
	public void addMembers(Set<T> set) {
		this.dendrogram1.addMembers(set);
		this.dendrogram2.addMembers(set);
	}
	
	
	
	/**
	 * Template method for adding leaves of dendrogram to given set
	 * 
	 * @param Set of leaves to add leaves of current dendrogram to
	 */
	public void addLeaves(Set<LeafDendrogram<T>> leaves) {
		this.dendrogram1.addLeaves(leaves);
		this.dendrogram2.addLeaves(leaves);
	}
	
	
	
	/**
	 * Returns first child of this inner node
	 * 
	 * @return First child of this node
	 */
	public Dendrogram<T> dendrogram1() {
		return this.dendrogram1;
	}
	
	
	
	/**
	 * Returns second child of this inner node
	 * 
	 * @return Second child of this node
	 */
	public Dendrogram<T> dendrogram2() {
		return this.dendrogram2;
	}



	@Override
	void split(HashSet<Dendrogram<T>> resultSet, LinkedList<Dendrogram<T>> queue) {
		queue.add(this.dendrogram1());
		queue.add(this.dendrogram2());
	}



	/**
	 * Returns false, as this is an inner node and no leaf
	 * 
	 * @Override
	 */
	public boolean isLeaf() {
		return false;
	}
	
	
	
	/**
	 * Creates tag cloud for this inner node by combining tag clouds
	 * of both children.
	 */
	protected void createTagCloud() {
		this.tagCloud = new DefaultTagCloud(this.dendrogram1.tagCloud());
		this.tagCloud.addTags(this.dendrogram2.tagCloud().tags());
	}
	
}
