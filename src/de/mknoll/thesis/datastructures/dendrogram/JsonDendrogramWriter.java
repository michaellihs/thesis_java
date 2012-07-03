package de.mknoll.thesis.datastructures.dendrogram;

import java.util.HashSet;
import java.util.Stack;

import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Dendrogram writer for writing dendrograms in JSON format
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class JsonDendrogramWriter<T> {

	public String write(Dendrogram<T> dendrogram) {
		String json = this.writeToJson(dendrogram);
		return json;
	}

	
	
	private String writeToJson(Dendrogram<T> dendrogram) {
		String json = "{";
		
		// Holds a stack of yet to visit nodes
		Stack<Dendrogram<T>> dfsStack = new Stack<Dendrogram<T>>();
		
		// Holds a set of already visited nodes
		HashSet<Dendrogram<T>> visitedNodes = new HashSet<Dendrogram<T>>();
		
		// Set to true, if first inner node has been visited
		Boolean firstInner = true;
		
		dfsStack.push(dendrogram);
		
		Integer nodeCounter = 1;
		
		while (dfsStack.size() > 0) {
			Dendrogram<T> currentDendrogram = dfsStack.pop();
			if (currentDendrogram.size() > 1) {
				// We have an inner node (link) in dendrogram
				if (visitedNodes.contains(currentDendrogram)) {
					// This is our second visit to this inner node
					json += "},";
				} else {
					// This is our first visit to this inner node
					json += nodeCounter.toString() + ":{";
					dfsStack.push(currentDendrogram);
					visitedNodes.add(currentDendrogram);
					dfsStack.push(((LinkDendrogram<T>) currentDendrogram).dendrogram2());
					dfsStack.push(((LinkDendrogram<T>) currentDendrogram).dendrogram1());
					firstInner = true;
				}
				
			} else {
				// We have a leaf in dendrogram
				json += "'" + ((LeafDendrogram<T>)currentDendrogram).object().toString() + "'" + ":" + ((LeafDendrogram<RecommenderObject>)currentDendrogram).object().internalId();
				json += ",";
			}
			nodeCounter++;
		}
		json += "}";
		return json;
	}
	
}
