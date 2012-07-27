package de.mknoll.thesis.datastructures.graph;



/**
 * Interface for objects that can be attached to a node.
 * 
 * A class implementing this interface  must offer a method that is
 * called whenever an instance of this class is added to a node.
 * 
 * E.g. properties provided by this object can be added to the node
 * or external IDs etc are set on the node.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public interface AttachableToNode extends NodeProperty {

	/**
	 * Method is called whenever an object is attached to given node
	 * 
	 * @param node
	 */
	public void attachTo(Node node);
	
	
	
	/**
	 * Returns node to which object is attached to
	 * 
	 * @return Node to which object is attached to
	 */
	public Node getNode();

	
	
	/**
	 * Returns identifier for this object
	 * 
	 * @return
	 */
	public String identifier();
	
}
