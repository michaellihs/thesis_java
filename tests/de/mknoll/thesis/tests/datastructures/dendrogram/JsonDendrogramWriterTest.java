package de.mknoll.thesis.tests.datastructures.dendrogram;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mknoll.thesis.datastructures.dendrogram.JsonDendrogramWriter;
import de.mknoll.thesis.datastructures.dendrogram.LeafDendrogram;
import de.mknoll.thesis.datastructures.dendrogram.LinkDendrogram;
import de.mknoll.thesis.datastructures.graph.RecommenderObject;



/**
 * Testcase for json dendrogram writer
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class JsonDendrogramWriterTest {
	
	@Test
	public void writeDendrogramReturnsExpectedStringOnSimpleDendrogram() {
		RecommenderObject recObject1 = new RecommenderObject("1","1","1"); recObject1.setInternalId(1);
		RecommenderObject recObject2 = new RecommenderObject("2","2","2"); recObject2.setInternalId(2);
		JsonDendrogramWriter<RecommenderObject> writer = new JsonDendrogramWriter<RecommenderObject>();
		String json = writer.write(
				new LinkDendrogram<RecommenderObject>(
						new LeafDendrogram<RecommenderObject>(recObject1), 
						new LeafDendrogram<RecommenderObject>(recObject2)
				)
		);
		                        
		assertTrue(json.equals("{1:{'1 - 1(1)':1,'2 - 2(2)':2,},}"));
	}
	
	
	
	@Test
	public void writeDendrogramReturnsExpectedStringOnComplexDendrogram() {
		RecommenderObject recObject1 = new RecommenderObject("1","1","1"); recObject1.setInternalId(1);
		RecommenderObject recObject2 = new RecommenderObject("2","2","2"); recObject2.setInternalId(2);
		RecommenderObject recObject3 = new RecommenderObject("3","3","3"); recObject3.setInternalId(3);
		RecommenderObject recObject4 = new RecommenderObject("4","4","4"); recObject4.setInternalId(4);
		JsonDendrogramWriter<RecommenderObject> writer = new JsonDendrogramWriter<RecommenderObject>();
		String json = writer.write(
				new LinkDendrogram<RecommenderObject>(
						new LinkDendrogram<RecommenderObject>(
								new LeafDendrogram<RecommenderObject>(recObject1),
								new LeafDendrogram<RecommenderObject>(recObject2)
						), 
						new LinkDendrogram<RecommenderObject>(
								new LeafDendrogram<RecommenderObject>(recObject3),
								new LeafDendrogram<RecommenderObject>(recObject4)
						)
				)
		);
		
		assertTrue(json.equals("{1:{2:{'1 - 1(1)':1,'2 - 2(2)':2,},6:{'3 - 3(3)':3,'4 - 4(4)':4,},},}"));
	}
	
}
