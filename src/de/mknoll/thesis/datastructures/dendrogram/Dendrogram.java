package de.mknoll.thesis.datastructures.dendrogram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mcavallo.opencloud.Cloud;

import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;



/**
 * Class implements a dendrogram data structure
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public abstract class Dendrogram<T extends TagCloudContainer> {

	/**
	 * Holds parent of dendrogram
	 */
	private LinkDendrogram<T> parent;
	
	
	
	/**
	 * Holds reference link to most upper parent
	 */
	private Dendrogram<T> referenceLink;
	
	
	
	/**
	 * Holds a tag cloud of contained objects
	 */
	protected Cloud tagCloud = null;



	/**
	 * Holds id for Dendrogram (for debugging only!)
	 */
	protected String id;



	/**
	 * Holds entropy of clustering on this step within dendrogram
	 */
	private Double entropy;
	
	
	
	/**
	 * Constructor for dendrogram
	 */
	public Dendrogram() {
		this.referenceLink = this;
	}
	
	
	
	/**
	 * Constructor for dendrogram that takes an id (for debugging and development only)
	 * @param id
	 */
	public Dendrogram(String id) {
		this();
		this.id = id;
	}
	
	
	
	/**
	 * Returns parent dendrogram of this dendrogram
	 * 
	 * @return Parent dendrogram of this dendrogram
	 */
	public LinkDendrogram<T> parent() {
		return this.parent;
	}
	
	
	
	/**
	 * Returns true, if given element is contained in dendrogram
	 * 
	 * @param element
	 * @return
	 */
	public boolean contains(T element) {
		return this.memberSet().contains(element);
	}
	
	
	
	/**
	 * Returns true, if dendrogram is link
	 * 
	 * @return True, if dendrogram is link
	 */
	public boolean isLink() {
		return !this.isLeaf();
	}
	
	
	
	/**
	 * Returns true, if dendrogram is leaf
	 * 
	 * @return True, if dendrogram is leaf
	 */
	abstract public boolean isLeaf();
	


	/**
	 * Returns root of dendrogram
	 * 
	 * @return Most upper parent of dendrogram
	 */
	public Dendrogram<T> dereference() {
		Dendrogram<T> ancestor = this.referenceLink.parent();  
        if (ancestor == null) return this.referenceLink;  
        for (LinkDendrogram<T> nextAncestor = null;  
             (nextAncestor = ancestor.parent()) != null;  
             ancestor = nextAncestor);  
        this.referenceLink = ancestor;  
        return this.referenceLink; 
	}
	
	
	
	/**
	 * Returns number of contained objects
	 * 
	 * @return Number of contained objects
	 */
	public Integer size() {
		// TODO think about how to cache this!
		return this.memberSet().size();
	}
	
	
	
	/**
	 * Returns a partitioning of given size.
	 * 
	 * Partitioning a dendrogram is like cutting the dendrogram's tree
	 * at a certain level and gathering the membersets of the subtrees in
	 * a set.
	 * 
	 * @param Number of partitions to be created. Must be bigger than 1!
	 * @return Partitioning consisting of a set of membersets
	 * @throws IllegalArgumentException if number of partitions is less than 1
	 * @throws IllegalArgumentException if number of partitions is bigger than contained elements
	 */
	public Set<Set<T>> partition(Integer k) {
		Set<Dendrogram<T>> dendrograms = this.partitionDendrogram(k);
		Set<Set<T>> resultSet = new HashSet<Set<T>>(k);
		for(Dendrogram<T> d : dendrograms) {
			resultSet.add(d.memberSet());
		}
		return resultSet;
	}
	
	
	
	/**
	 * Returns a set of dendrograms with size k generated by "cutting" dendrogram
	 * such that k sub-dendrograms are generated.
	 * 
	 * @param Number of partitions to be created. Must be bigger than 1!
	 * @return Partitioning consisting of a set of membersets
	 * @throws IllegalArgumentException if number of partitions is less than 1
	 * @throws IllegalArgumentException if number of partitions is bigger than contained elements
	 */
	public Set<Dendrogram<T>> partitionDendrogram(Integer k) {
		if (k <= 1) {
			throw new IllegalArgumentException("Number of partitions must be bigger than 1!");
		}
		if (this.size() < k) {
			throw new IllegalArgumentException("This dendrograms only contains " + this.size() + " elements. You asked me to generate " + k + " partitions.");
		}
		
		// TODO original implementation has priority queue here, to select split with higher priority!
		LinkedList<Dendrogram<T>> queue = new LinkedList<Dendrogram<T>>();
		HashSet<Dendrogram<T>> resultSet = new HashSet<Dendrogram<T>>(k);
		
		queue.add(this);
		
		while(queue.size() + resultSet.size() < k) {
			Dendrogram<T> toSplit = queue.pop();
			toSplit.split(resultSet, queue);
		}
		
		for(Dendrogram<T> d : queue) {
			resultSet.add(d);
		}
		
		return resultSet;
	}
	
	
	
	public Cloud tagCloud() {
		if (this.tagCloud == null) {
			this.createTagCloud();
		}
		return this.tagCloud;
	}
	
	
	
	/**
	 * Returns depth of current node within dendrogram (path length to root)
	 * 
	 * @return Depth of current node
	 */
	public Integer depth() {
		Dendrogram<T> parent = this.parent;
		Integer depth = 0;
		while (parent != null) {
			depth++;
			parent = parent.parent();
		}
		return depth;
	}
	
	
	
	/**
	 * Returns list of cluster sizes one for each step upwards within dendrogram
	 * 
	 * @return List of cluster sizes one for each step upwards within dendrogram
	 */
	public List<Integer> clusterSizes() {
		Dendrogram<T> parent = this.parent;
		List<Integer> clusterSizes = new ArrayList<Integer>();
		while (parent != null) {
			clusterSizes.add(parent.size());
			parent = parent.parent();
		}
		return clusterSizes;
	}
	
	
	
	/**
	 * Returns list of entropies from given node upwards within dendrogram
	 * 
	 * @return List of entropies from given node upwards within dendrogram
	 */
	public List<Double> entropies() {
		Dendrogram<T> parent = this.parent;
		List<Double> entropies = new ArrayList<Double>();
		entropies.add(this.entropy());
		while (parent != null) {
			entropies.add(parent.entropy());
			parent = parent.parent();
		}
		return entropies;
	}
	
	
	
	/**
	 * Returns entropy of clustering on current step within dendrogram
	 * 
	 * @return Entropy of clustering on current step within dendrogram
	 */
	public Double entropy() {
		return this.entropy;
	}
	
	
	
	/**
	 * Sets entropy on current step within histogram
	 * 
	 * @param Entropy on current step within histogram
	 */
	public void setEntropy(Double entropy) {
		this.entropy = entropy;
	}
	
	
	
	abstract protected void createTagCloud();



	/**
	 * Template method for getting member set of dendrogram
	 * 
	 * @return Members of dendrogram
	 */
	abstract public Set<T> memberSet();
	
	
	
	/**
	 * Template method for getting leaf set of dendrogram
	 * 
	 * @return Leaves of dendrogram
	 */
	abstract public Set<LeafDendrogram<T>> leaves();

	
	
	/**
	 * Template method for adding members of dendrogram to given set
	 * 
	 * @param Set to add members of dendrogram to
	 */
	abstract public void addMembers(Set<T> set);
	
	
	
	/**
	 * Template method for adding leaves of dendrogram to given set
	 * 
	 * @param Set of leaves to add leaves of current dendrogram to
	 */
	abstract public void addLeaves(Set<LeafDendrogram<T>> leaves);
	
	
	
	/**
	 * Does a split on dendrogram. 
	 * 
	 * @param resultSet
	 * @param queue
	 */
	abstract void split(HashSet<Dendrogram<T>> resultSet, LinkedList<Dendrogram<T>> queue);
	
	
	
	/**
	 * Sets parent to given dendrogram
	 * 
	 * @param Dendrogram to be set as parent
	 */
	protected void setParent(LinkDendrogram<T> parent) {
		this.parent = parent;
		this.referenceLink = parent;
	}
	
}
