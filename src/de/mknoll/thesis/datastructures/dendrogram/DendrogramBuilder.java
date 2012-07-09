package de.mknoll.thesis.datastructures.dendrogram;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Class implements a dendrogram builder.
 * 
 * Dendrograms are built up getting pairs of nodes which have to be merged. 
 * For each pair of merged IDs, a new dendrogram (a tree) is created and put
 * into an array of dendrograms (a forest). At the end only a single dendrogram
 * should be left over since the forest should be merged to a tree.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.dendrogram.RecommenderObjectDendrogramBuilderTest
 */
public abstract class DendrogramBuilder<T> {
	
	/**
	 * Map to identify each dendrogram within the forest of dendrograms
	 */
	protected HashMap<String, Dendrogram<T>> idToDendrogramMap;
	
	
	
	/**
	 * List of dendrograms left to be merged
	 */
	private ArrayList<Dendrogram<T>> dendrograms;
	
	
	
	/**
	 * Holds a list of steps performed during building of histogram and its nodes
	 * This can later be used for getting some statistical data on histogram
	 * building process.
	 */
	private ArrayList<LinkDendrogram<T>> steps;
	
	
	
	/**
	 * Constructor for dendrogram builder
	 */
	public DendrogramBuilder() {
		this.dendrograms = new ArrayList<Dendrogram<T>>();
		this.idToDendrogramMap = new HashMap<String, Dendrogram<T>>();
		this.steps = new ArrayList<LinkDendrogram<T>>();
	}
	
	
	
	/**
	 * Merges two dendrograms identified by given ids
	 * 
	 * An id is an external (generated) ID here!
	 * 
	 * @param Id to be found within first dendrogram to be merged
	 * @param Id to be found within second dendrogram to be merged
	 * @throws Exception 
	 */
	abstract public void mergeByIds(String id1, String id2) throws Exception;
	


	/**
	 * Returns finished dendrogram or throws an exception if there
	 * is still a forest of dendrograms left over that need to be merged.
	 * 
	 * @return Finished dendrogram
	 * @throws Exception if more than one dendrogram is left over in the forest.
	 */
	public LinkDendrogram<T> getDendrogram() throws Exception {
		if (this.dendrograms.size() == 1) {
			return (LinkDendrogram<T>) this.dendrograms.get(0); 
		} else {
			throw new Exception("You asked me to return an incomplete dendrogram. Still " + this.dendrograms.size() + " dendrograms left to merge!");
		}
	}
	
	

	/**
	 * Returns biggest dendrogram currently in forrest of dendrograms
	 * 
	 * @return Biggest dendrogram
	 */
	public Dendrogram<T> getBiggestDendrogram() {
		Dendrogram<T> biggestDendrogram = this.dendrograms.get(0);
		for (Dendrogram<T> dendrogram:this.dendrograms) {
			if (dendrogram.size() > biggestDendrogram.size()) {
				biggestDendrogram = dendrogram;
			}
		}
		return biggestDendrogram;
	}
	
	
	
	/**
	 * Returns currently listed dendrograms
	 * 
	 * @return
	 */
	public ArrayList<Dendrogram<T>> getDendrograms() {
		return this.dendrograms;
	}
	
	
	
	/**
	 * Returns a list of steps describing building process of dendrogram.
	 * 
	 * For each step in histogram creation, a step is added that 
	 * lets you examine what happens at each step.
	 * 
	 * @return List of dendrogram nodes that are created at each step.
	 */
	public ArrayList<LinkDendrogram<T>> steps() {
		return this.steps;
	}

	
	
	/**
	 * Returns a dendrogram for a given id.
	 * 
	 * Dendrogram is taken from map of dendrograms (if it exists for given id)
	 * or a new dendrogram leaf is created for given id and put into the map.
	 * 
	 * An id is an external (generated) ID here!
	 * 
	 * @param ID to get dendrogram for (the dendrogram that actually contains the id)
	 * @return Dendrogram found or created for given id
	 * @throws Exception 
	 */
	protected Dendrogram<T> getDendrogramById(String id) throws Exception {
		Dendrogram<T> dendrogram;
		if (this.idToDendrogramMap.containsKey(id)) {
			dendrogram = this.idToDendrogramMap.get(id);
		} else {
			T t = this.getInstanceOfTypeObject(id);
			dendrogram = new LeafDendrogram<T>(t);
			this.idToDendrogramMap.put(id, dendrogram);
			this.dendrograms.add(dendrogram);
		}
		return dendrogram;
	}


	
	/**
	 * 
	 * 
	 * @param dendrogramById
	 * @param dendrogramById2
	 */
	protected LinkDendrogram<T> mergeDendrograms(Dendrogram<T> dendrogram1, Dendrogram<T> dendrogram2) {
		
		this.dendrograms.remove(dendrogram1);
		this.dendrograms.remove(dendrogram2);
		
		LinkDendrogram<T> mergedDendrogram = new LinkDendrogram<T>(dendrogram1, dendrogram2);
		this.steps.add(mergedDendrogram);
		
		//this.removeIdsFromMapByGivenDendrogram(mergedDendrogram);
		//this.addIdsToMapByGivenDendrogram(mergedDendrogram);
		
		this.dendrograms.add(mergedDendrogram);
		
		return mergedDendrogram;
	}



	/**
	 * Template method to be implemented by concrete implementation of this class.
	 * 
	 * @return Object of generic type T to be used in dendrogram
	 * @throws Exception 
	 */
	protected abstract T getInstanceOfTypeObject(String id) throws Exception;
	
	

	/**
	 * Template method for getting ID from object contained by dendrogram
	 * 
	 * @param Object to get ID from
	 * @return ID of given object
	 */
	protected abstract String getIdFromObject(T object);
	
}
