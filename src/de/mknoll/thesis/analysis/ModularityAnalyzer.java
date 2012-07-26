package de.mknoll.thesis.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.mknoll.thesis.datastructures.graph.Node;
import de.mknoll.thesis.datastructures.graph.RecommendationGraph;



/**
 * Class implements an analyzer for calculating modularity
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 * @see de.mknoll.thesis.tests.analysis.ModularityAnalyzerTest
 */
public class ModularityAnalyzer {

	/**
	 * Holds recommendation graph to calculate modularity upon
	 */
	private RecommendationGraph graph;
	
	
	
	/**
	 * Constructor takes recommendation graph to calculate modularity upon
	 * 
	 * @param recommendationGraph
	 */
	public ModularityAnalyzer(RecommendationGraph recommendationGraph) {
		this.graph = recommendationGraph;
	}
	
	
	
	/**
	 * Calculates modularity for given clustering on graph set via constructor
	 * 
	 * @param Clustering to calculate modularity for
	 * @return Modularity for given clustering
	 * @throws Exception if internal IDs of nodes in graph are not within range [1...|V|] where V is vertex set of graph 
	 */
	public Double modularity(HashSet<HashSet<Node>> clustering) throws Exception {
		int clusterCount = clustering.size();
		
		int[] clustermap = new int[this.graph.vertexSet().size()]; // maps vertex_id -> cluster_id
		Iterator<HashSet<Node>> clusterIterator = clustering.iterator();
		int currentCluster = 0; // We start counting clusters at 0
		
		// We initialize cluster map that holds clusterMap[node.internalId] = clusterId
		while(clusterIterator.hasNext()) {
			Set<Node> cluster = clusterIterator.next();
			Iterator<Node> nodeIterator = cluster.iterator();
			while(nodeIterator.hasNext()) {
				Node node = nodeIterator.next();
				if (node.internalId() > clustermap.length + 1) {
					// Check whether internal node id is within range
					throw new Exception("Internal ID of node (" + node.internalId() + ") was bigger than size of node-to-vector array. Seems like internal IDs of given graph are not within [1..." + this.graph.vertexSet().size() + "]");
				}
				clustermap[node.internalId()-1] = currentCluster;
			}
			currentCluster++;
		}
		
		ArrayList<HashMap<Integer, Double>> e = new ArrayList<HashMap<Integer, Double>>();
		for (int i = 0; i < clusterCount; i++) {
			e.add(new HashMap<Integer, Double>());
		}
		
		long edgeCount = 0;
		
		for (Node n : this.graph.vertexSet()) {
			Set<Node> neighbors = this.graph.getNeighborsOf(n);
			for (Node k : neighbors) {
				// n corresponds to i, k corresponds to j
				if (n.internalId() == k.internalId()) continue;  // Disregard loops
				Integer from = clustermap[n.internalId() - 1];
				Integer to = clustermap[k.internalId() -  1];
				
				if (e.get(from).containsKey(to)) {
					e.get(from).put(to, e.get(from).get(to) + 1.0);
				} else {
					e.get(from).put(to, 1.0);
				}
			}
			edgeCount++;
		}
		
		double[] a = new double[clusterCount];
		for (int i = 0; i < clusterCount; i++) {
			a[i] = 0.0;
			for (Integer column : e.get(i).keySet()) {
				e.get(i).put(column, e.get(i).get(column) / edgeCount);
				a[i] += e.get(i).get(column);
			}
		}
		
		double Q = 0.0;
		for (int i = 0; i < clusterCount; i++) {
			if (e.get(i).containsKey(i)) {
				Q += e.get(i).get(i) - a[i] * a[i];
			} else {
				Q += 0 - a[i] * a[i];
			}
		}
		return Q;
		
	}
	
}

/*
double RGModularityClusterer::getModularityFromClustering(Graph* graph, Partition* clusters) {
    int clusterCount = clusters->getPartitionVector()->size();

    long counter = 0;
    int* clustermap = new int[graph->getVertexCount()]; // maps vertex_id -> cluster_id
    
    
    // Initializes cluster map: vertex --> clusterId
    for (int i = 0; i < clusterCount; i++) {
        idlist* cluster = clusters->getPartitionVector()->at(i);

        long csize = 0;
        for (idlist::iterator iter = cluster->begin(); iter != cluster->end(); ++iter) {
            long vertexid = *iter;
            clustermap[vertexid] = i;
            csize++;
        }
    }
	
	// Define new datatype int --> double
    typedef boost::unordered_map<int,double> doublevector;
    
    // Define new datatype int --> int --> double
    typedef std::vector<doublevector*> doubledoublevector;

    doubledoublevector* e = new doubledoublevector();
    
    // Initialize a vector of int --> double pairs for each cluster
    for (int i = 0; i < clusterCount; i++) {
        doublevector* dv = new doublevector();
        e->push_back(dv);
    }

    long edgeCount = 0; // will be 2*|E|
    
    // For each vertex i within graph
    for (long i = 0; i < graph->getVertexCount(); i++) {
        intvector* neighbors = graph->getNeighbors(i);
        
        // For each neighbor of a vertex i within the graph
        for (long j = 0; j < neighbors->size(); j++) {
            if (i == neighbors->at(j)) continue; // disregard loops

			// Get cluster to which vertex i belongs
            long from = clustermap[i];
            
            // Get cluster to which vertex j belongs
            long to = clustermap[neighbors->at(j)];
            
	    	if (e->at(from)->find(to) != e->at(from)->end())
	    		// If a mapping for vertex i already exists, take this one
				e->at(from)->at(to) += 1.0;
	    	else
	    		// else create a new mapping for vertex i
				e->at(from)->insert(std::make_pair(to,1.0));	

            edgeCount++;
        }
    }

	// Create an array which holds "modularity for each cluster"
    double a[clusterCount];
    for (int i = 0; i < clusterCount; i++) {
        a[i] = 0.0;

    	for (doublevector::iterator iter = e->at(i)->begin(); iter != e->at(i)->end(); ++iter) {
            long column = iter->first;
            e->at(i)->at(column) /= (double) edgeCount;
            a[i] += e->at(i)->at(column);
        }
    }

    double Q = 0.0;
    for (int i = 0; i < clusterCount; i++)
		if (e->at(i)->find(i) != e->at(i)->end())
	        	Q += e->at(i)->at(i) - a[i] * a[i];
		else
			Q += 0 - a[i] * a[i];

    delete [] clustermap;
    for (int i = 0; i < e->size(); i++)
        delete e->at(i);
    delete e;

    return Q;
}
*/