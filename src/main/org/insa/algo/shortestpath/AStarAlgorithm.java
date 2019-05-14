package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Label;
import org.insa.graph.LabelStar;
import org.insa.graph.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public ArrayList<Label> Initialisation(ShortestPathData data, BinaryHeap<Label> tas) {
    	List<Node> list_nodes = data.getGraph().getNodes();
    	ArrayList<Label> list_label = new ArrayList<Label>();
    	for(Node current_node: list_nodes) {
    		list_label.add(new LabelStar(current_node.getId(),data));
    	}
    	
    	Node origin = data.getOrigin();
    	list_label.get(origin.getId()).setCost(0);
    	tas.insert(list_label.get(origin.getId()));
    	
    	// Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
    	
    	return list_label;
    }

}
