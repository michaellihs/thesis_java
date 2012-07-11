package de.mknoll.thesis.datastructures.dendrogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

import de.mknoll.thesis.datastructures.graph.RecommenderObject;
import de.mknoll.thesis.datastructures.tagcloud.TagCloudContainer;
import de.mknoll.thesis.framework.filesystem.FileManager;
import de.mknoll.thesis.framework.logger.LoggerInterface;



/**
 * Dendrogram writer for writing dendrograms in JSON format
 *  
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class XmlDendrogramWriter<T extends TagCloudContainer> {
	
	/**
	 * Holds logger
	 */
	private LoggerInterface logger;
	
	
	
	/**
	 * Holds file manager for handling file creation and naming
	 */
	private FileManager fileManager;
	
	
	
	/**
	 * Constructor takes logger as argument
	 * 
	 * @param logger
	 */
	public XmlDendrogramWriter(LoggerInterface logger, FileManager fileManager) {
		this.logger = logger;
		this.fileManager = fileManager;
	}

	
	
	/**
	 * Writes given dendrogram directly into file
	 * TODO specify file as parameter
	 * 
	 * @param dendrogram
	 * @return
	 * @throws Exception 
	 */
	public String write(Dendrogram<T> dendrogram) throws Exception {
		this.writeToXml(dendrogram);
		return "finished";
	}

	
	
	private void writeToXml(Dendrogram<T> dendrogram) throws Exception {
		// TODO put all this into a writer class
        BufferedWriter bufferedWriter = null;
        try {
                bufferedWriter = new BufferedWriter( new FileWriter(this.fileManager.getResultFilePath("dendrogram.xml")));
                
                bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dendrogram>\n");
        		Integer indentationFactor = 1;
        		
        		// Holds a stack of yet to visit nodes
        		Stack<Dendrogram<T>> dfsStack = new Stack<Dendrogram<T>>();
        		
        		// Holds a set of already visited nodes
        		HashSet<Dendrogram<T>> visitedNodes = new HashSet<Dendrogram<T>>();
        		
        		dfsStack.push(dendrogram);
        		
        		Integer nodeCounter = 1;
        		
        		while (dfsStack.size() > 0) {
        			Dendrogram<T> currentDendrogram = dfsStack.pop();
        			if (currentDendrogram.size() > 1) {
        				// We have an inner node (link) in dendrogram
        				if (visitedNodes.contains(currentDendrogram)) {
        					// This is our second visit to this inner node
        					indentationFactor--;
        					bufferedWriter.write(this.indent(indentationFactor) + "</link>\n");
        				} else {
        					// This is our first visit to this inner node
        					bufferedWriter.write(this.indent(indentationFactor) + "<link nodeId=\"" + nodeCounter.toString() + "\">\n");
        					dfsStack.push(currentDendrogram);
        					visitedNodes.add(currentDendrogram);
        					dfsStack.push(((LinkDendrogram<T>) currentDendrogram).dendrogram2());
        					dfsStack.push(((LinkDendrogram<T>) currentDendrogram).dendrogram1());
        					indentationFactor++;
        				}
        				
        			} else {
        				// We have a leaf in dendrogram
        				bufferedWriter.write(this.indent(indentationFactor) + "<leaf internalId=\"" + ((LeafDendrogram<RecommenderObject>)currentDendrogram).object().internalId() + "\">" + ((LeafDendrogram<T>)currentDendrogram).object().toString() + "</leaf>\n");
        			}
        			// this.logger.log("Wrote node id " + nodeCounter + " with indentation " + indentationFactor);
        			nodeCounter++;
        		}
        		bufferedWriter.write("</dendrogram>\n");

        } catch ( IOException e) {
        } finally {
                try {
                        if ( bufferedWriter != null)
                                bufferedWriter.close( );
                } catch ( IOException e) {
                }
        }
		
	}
	
	
	
	private String indent(Integer level) {
		return "";/*
		String singleIndentation = "";
		String indentation = "";
		for (int i = 0; i < level; i++) {
			indentation += singleIndentation;
		}
		
		return indentation; */
	}
	
}
