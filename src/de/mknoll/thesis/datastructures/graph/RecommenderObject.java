package de.mknoll.thesis.datastructures.graph;

import java.util.HashMap;
import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.tagcloud.DefaultTagCloud;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;
import de.mknoll.thesis.datastructures.tagcloud.TagExtractor;



/**
 * Class implements a recommender object.
 * 
 * Within the recommendation graph, this object acts as a node.
 * It holds all information required for a document within the
 * recommender service.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.datastructures.graph.RecommenderObjectTest
 */
public class RecommenderObject 
		implements AttachableToNode, TagCloudContainer {

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
	 * Holds tags for this recommender object
	 * 
	 * Tags are filtered!
	 */
	private Tag[] tags = null;



	/**
	 * Holds tags as string array
	 * 
	 * Tags are filtered!
	 */
	private String[] tagsAsStrings;
	
	
	
	/**
	 * Holds leaf of dendrogram to which this recommender object is attached
	 */
	private LeafDendrogram<RecommenderObject> leafDendrogram;
	
	
	
	/**
	 * Constructor for anonymous recommender object
	 */
	public RecommenderObject() {
		this("", "", "");
	}
	
	
	
	/**
	 * Constructor for recommender object for a given id
	 * 
	 * @param docId
	 */
	public RecommenderObject(String docId) {
		this(docId, "", "");
	}
	
	
	
	/**
	 * Constructor for recommender object for a given id and a description (title)
	 * 
	 * @param docId
	 * @param description
	 */
	public RecommenderObject(String docId, String description) {
		this(docId, description, "");
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
	 * Returns node to which this recommender object is attached
	 * 
	 * @return
	 */
	public Node getNode() {
		return this.node;
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
	public HashMap<String, Object> getProperties() {
		if (this.tags == null) {
			this.createTags();
		}
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("tags", this.tagsAsStrings);
		properties.put("description", this.description);
		properties.put("isbn12", this.isbn12);
		properties.put(DefaultNamespaces.BIBTIP.toString(), this.docId);
		properties.put("internalId", new Integer(this.node.internalId()).toString());
		return properties;
	}
	
	
	
	/**
	 * Sets given leaf dendrogram as dendrogram to which this recommender object is attached to
	 * 
	 * @param leafDendrogram
	 */
	public void setDendrogramLeaf(LeafDendrogram<RecommenderObject> leafDendrogram) {
		this.leafDendrogram = leafDendrogram;
	}
	
	
	
	/**
	 * Returns leaf dendrogram to which this recommender object is attached to
	 * 
	 * @return Leaf dendrogram to which this recommender object is attached to
	 */
	public LeafDendrogram<RecommenderObject> getDendrogramLeaf() {
		return this.leafDendrogram;
	}
	
	
	
	/**
	 * Returns a list of cluster sizes, one for each step upwards within the histogram
	 * 
	 * @return Cluster sizes one for each step within histogram
	 */
	public List<Integer> clusterSizes() {
		return this.leafDendrogram.clusterSizes();
	}



	/**
	 * Reads recommender object's title and creates an array of tags for 
	 * this object.
	 */
	private void createTags() {
		TagExtractor extractor = new TagExtractor();
		DefaultTagCloud cloud = new DefaultTagCloud();
		cloud.addTags(extractor.extractTags(this.description));
		Tag[] tags = new Tag[cloud.tags().size()];
		this.tags = cloud.tags().toArray(tags); 
		this.tagsAsStrings = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			this.tagsAsStrings[i] = this.tags[i].getName();
		}
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
	
	
	
	/**
	 * Returns array of tags for this recommender object
	 * 
	 * @return Array of tags for this recommender object
	 */
	public Tag[] tags() {
		return this.tags;
	}
	
	
	
	/**
	 * Returns tags as array of strings for this recommender object
	 * 
	 * @return Tags as array of strings for this recommender object
	 */
	public String[] tagsAsStrings() {
		return this.tagsAsStrings;
	}



	/**
	 * Returns a tag cloud for this recommender object
	 * 
	 * @Override
	 */
	public Cloud getTagCloud() {
		Cloud tagCloud = new DefaultTagCloud();
		if (this.tags == null) {
			this.createTags();
		}
		for (int i = 0; i < this.tags.length; i++) {
			tagCloud.addTag(this.tags[i]);
		}
		return tagCloud;
	}
	
}
