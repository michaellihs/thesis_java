package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;



/**
 * Class implements a recommender object.
 * 
 * Within the recommendation graph, this object acts as a node.
 * It holds all information required for a document within the
 * recommender service.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 *
 */
public class RecommenderObject 
		implements AttachableToNode {

	/**
	 * Holds property name if this object is attached to a node
	 */
	private static final String NODE_PROPERTY_NAME = "recommender_object";



	/**
	 * Holds identifier for this class if attached to a node
	 */
	public static final String NODE_IDENTIFIER = RecommenderObject.NODE_PROPERTY_NAME;



	/**
	 * Holds internal doc id (nd) for this recommender object
	 */
	private String docId;
	
	
	
	/**
	 * Holds ISBN for this recommender object
	 */
	private String isbn12;
	
	
	
	/**
	 * Hods description for this recommender object
	 */
	private String description;
	
	
	
	/**
	 * Holds internal id of recommendation if numeric ID is required
	 */
	private Integer internalId;



	/**
	 * Holds node to which recommender object is attached to
	 */
	private Node node;
	
	
	
	/**
	 * Constructor for anonymous recommender object
	 */
	public RecommenderObject() {
		this("", "", "");
	}
	
	
	
	/**
	 * Constructor for recommender object for a given id
	 * 
	 * @param id
	 */
	public RecommenderObject(String id) {
		this(id, "", "");
	}
	
	
	
	/**
	 * Constructor for recommender object
	 * 
	 * @param docId Internal doc id for this recommender object
	 * @param description Description for this recommender object
	 * @param isbn12 ISBN for this recommender object
	 */
	public RecommenderObject(String docId, String description, String isbn12) {
		this.docId = docId;
		this.description = description;
		this.isbn12 = isbn12;
	}



	/**
	 * Getter for internal doc id
	 * 
	 * @return
	 */
	public String getDocId() {
		return docId;
	}



	/**
	 * Getter for ISBN 
	 * 
	 * @return
	 */
	public String getIsbn12() {
		return isbn12;
	}


	
	/**
	 * Getter for description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	
	
	@Override
	public String toString() {
		return this.description + " - " + this.isbn12 + "(" + this.getDocId() + ")";
	}
	
	
	
	/**
	 * Setter for internal id
	 */
	public void setInternalId(Integer internalId) {
		this.internalId = internalId;
	}
	
	
	
	/**
	 * Getter for internal id
	 * 
	 * @return
	 */
	public Integer internalId() {
		return this.internalId;
	}



	/**
	 * Attaches recommender object to given node
	 * 
	 * External IDs are set on node and properties are set on node.
	 * 
	 * @Override
	 */
	public void attachTo(Node node) {
		this.node = node;
		// We add external IDs provided by this object to node
		node.addExternalId(DefaultNamespaces.BIBTIP, this.docId);
		node.addProperty(RecommenderObject.NODE_PROPERTY_NAME, this);
	}



	@Override
	public HashMap<String, String> getProperties() {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("description", this.description);
		properties.put("isbn12", this.isbn12);
		properties.put(DefaultNamespaces.BIBTIP.toString(), this.docId);
		properties.put("internalId", new Integer(this.node.internalId()).toString());
		return properties;
	}



	/**
	 * Returns identifier for this object
	 * 
	 * @Override
	 */
	public String identifier() {
		return RecommenderObject.NODE_IDENTIFIER;
	}



	/**
	 * Returns true, if recommender object has empty doc id
	 * 
	 * @return True, if doc id is empty
	 */
	public boolean hasEmptyDocId() {
		if (this.docId == null || this.docId.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
}