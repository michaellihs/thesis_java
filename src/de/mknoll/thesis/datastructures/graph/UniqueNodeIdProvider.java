package de.mknoll.thesis.datastructures.graph;



/**
 * Class implements node id provider
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class UniqueNodeIdProvider implements NodeIdProvider {
	
	private static int START_ID = 1;
	
	
	
	/**
	 * Holds singleton instance of this class
	 */
	private static UniqueNodeIdProvider instance = null;
	
	
	
	/**
	 * Holds ID value to be incremented each time a unique ID is requested
	 */
	private int currentId;
	
	
	
	/**
	 * Returns singleton instance of node ID provider
	 * 
	 * @return
	 */
	public static UniqueNodeIdProvider getInstance() {
		if (UniqueNodeIdProvider.instance == null) {
			UniqueNodeIdProvider.instance = new UniqueNodeIdProvider();
		}
		return UniqueNodeIdProvider.instance;
	}
	
	
	
	/**
	 * Private constructor to force usage of singleton getInstance()
	 */
	private UniqueNodeIdProvider() {
		this.reset();
	}
	
	
	
	/**
	 * Returns unique ID
	 * 
	 * @return Unique ID
	 */
	public int getId() {
		return this.currentId++;
	}
	
	
	
	/**
	 * Resets id provider
	 */
	public void reset() {
		this.currentId = START_ID;
	}



	/**
	 * Returns first id created by this id provider
	 * 
	 * @return
	 */
	public int firstId() {
		return START_ID;
	}

}
