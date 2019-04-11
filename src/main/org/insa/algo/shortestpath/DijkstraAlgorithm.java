package org.insa.algo.shortestpath;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.*;
import java.util.*;
import java.lang.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public ArrayList<Label> Initialisation(ShortestPathData data, BinaryHeap<Node> tas) {
    	List<Node> list_nodes = data.getGraph().getNodes();
    	ArrayList<Label> list_label = new ArrayList<Label>();
    	for(Node current_node: list_nodes) {
    		list_label.add(new Label(current_node.getId()));
    	}
    	
    	Node origin = data.getOrigin();
    	list_label.get(origin.getId()).setCost(0);
    	tas.insert(origin);
    	
    	return list_label;
    }
    
    public void Iterations(ArrayList<Label> list_label, BinaryHeap<Node> tas) {
    	boolean all_marked = false;
    	while(!all_marked) { // ajouter find min destination
    		
    		Node min=tas.findMin();
    		tas.deleteMin();
    		list_label.get(min.getId()).setMark(true);
    		List<Arc> successors= min.getSuccessors();
    		for(Arc tmp_arc : successors) {
    			int idNewNode = tmp_arc.getDestination().getId();
    			if(!list_label.get(idNewNode).getMark()) {
    				float tmpCost = list_label.get(idNewNode).getCost();
    				float minCost = Math.min(tmpCost,list_label.get(min.getId()).getCost()+tmp_arc.getLength());
    				list_label.get(idNewNode).setCost(minCost);
    				if(tmpCost != minCost) {
    					tas.insert(tmp_arc.getDestination());
    					list_label.get(idNewNode).setFather(tmp_arc);
    				}
    			}
    		}
    		
    		all_marked = true;
    		for(Label tmp_label : list_label) {
    			if(!tmp_label.getMark()) {
    				all_marked = false;
    			}
    		}
    	}
    }
    
    public ShortestPathSolution findShortestPath(ShortestPathData data,	ArrayList<Label> list_label) {
    	List<Arc> shortestPath = new ArrayList<Arc>();
    	Node currentNode = data.getDestination();
    	while(currentNode != data.getOrigin()) {
    		Arc arc_parent = list_label.get(currentNode.getId()).getFather();
    		shortestPath.add(arc_parent);
    		currentNode = arc_parent.getOrigin();
    	}
    	Path pathSolution = new Path(data.getGraph(), shortestPath);
    	ShortestPathSolution solution = new ShortestPathSolution(data,Status.OPTIMAL,pathSolution);
    	return solution;
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        BinaryHeap<Node> tas_node = new BinaryHeap<Node>();
        ArrayList<Label> list_label = Initialisation(data,tas_node);
        Iterations(list_label,tas_node);
        
        solution = findShortestPath(data,list_label);
        
        return solution;
    }

}
