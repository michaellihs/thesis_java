package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import org.hibernate.type.descriptor.sql.SmallIntTypeDescriptor;

import de.mknoll.thesis.datastructures.graph.Recommendation;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;



/**
 * Class implements a leaf node within a dendrogram
 * 
 * A single object of parameterized object can be attached to a
 * dendrogram leaf node.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 *
 * @param <T> Type of object that should be stored within dendrogram
 * @see de.mknoll.thesis.tests.datastructures.dendrogram.LeafDendrogramTest
 */
public class LeafDendrogram<T extends TagCloudContainer> extends Dendrogram<T> {
	
	/**
	 * Holds object that is actually contained by this leaf
	 */
	private T containedObject;
	
	
	
	/**
	 * Constructor takes object to be contained by this leaf
	 * 
	 * @param Object to be contained by this leaf
	 */
	public LeafDendrogram(T containedObject) {
		super();
		this.containedObject = containedObject;
	}
	
	
	
	/**
	 * Constructor that allows setting of an id for debugging
	 * 
	 * @param containedObject
	 * @param id
	 */
	public LeafDendrogram(T containedObject, String id) {
		this(containedObject);
		this.id = id;
	}



	/**
	 * Returns contained object of this leaf
	 * 
	 * @return Contained object of this leaf
	 */
	public T object() {
		return this.containedObject;
	}
	
	
	
	/**
	 * Returns set of objects contained by this dendrogram
	 * 
	 * @return Set of single object contained by this dendrogram
	 */
	public Set<T> memberSet() {
		HashSet<T> set = new HashSet<T>();
		set.add(this.containedObject);
		return set; 
	}
	
	
	
	/**
	 * Adds objects contained by this dendrogram into given set
	 * 
	 * @param Set to add contained object to
	 */
	public void addMembers(Set<T> set) {
		set.add(this.containedObject);
	}



	@Override
	void split(HashSet<Dendrogram<T>> resultSet, LinkedList<Dendrogram<T>> queue) {
		resultSet.add(this);
	}



	@Override
	public boolean isLeaf() {
		return true;
	}
	
	
	
	/**
	 * Creates tag cloud of this dendrogram leaf node by using attached object's tag cloud
	 */
	protected void createTagCloud() {
		this.tagCloud = this.containedObject.getTagCloud();
	}

}
