package org.insa.algo.shortestpath;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.*;
import java.util.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public ArrayList<Label> Initialisation(ShortestPathData data, BinaryHeap<Label> tas) {
    	List<Node> list_nodes = data.getGraph().getNodes();
    	ArrayList<Label> list_label = new ArrayList<Label>();
    	for(Node current_node: list_nodes) {
    		list_label.add(new Label(current_node.getId()));
    	}
    	
    	Node origin = data.getOrigin();
    	list_label.get(origin.getId()).setCost(0);
    	tas.insert(list_label.get(origin.getId()));
    	
    	// Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
    	
    	return list_label;
    }
    
    public void Iterations(ArrayList<Label> list_label, BinaryHeap<Label> tas, ShortestPathData data) {
    	int nb_iterations=0;
    	while(!tas.isEmpty() && !list_label.get(data.getDestination().getId()).getMark()) {
    		nb_iterations++;
    		Label label_min = tas.deleteMin();
    		label_min.setMark(true);
    		List<Arc> successors = data.getGraph().get(label_min.getCurrentSummit()).getSuccessors();
    		for(Arc tmp_arc : successors) {
    			int idOriginNode = tmp_arc.getOrigin().getId();
    			int idNewNode = tmp_arc.getDestination().getId();
    			System.out.println("Cost = "+list_label.get(idOriginNode).getCost()+"\n");
    			if(!list_label.get(idNewNode).getMark()) {
    				if(list_label.get(idNewNode).getCost() > list_label.get(idOriginNode).getCost() + (float)data.getCost(tmp_arc)) {
    					list_label.get(idNewNode).setCost(list_label.get(idOriginNode).getCost() + (float)data.getCost(tmp_arc));
    					list_label.get(idNewNode).setFather(tmp_arc);
    					boolean exist = list_label.get(idNewNode).getBelongToHeap();
    					if(exist) {
    						tas.remove(list_label.get(idNewNode));
    						tas.insert(list_label.get(idNewNode));
    					}
    					else {
    						tas.insert(list_label.get(idNewNode));
    						list_label.get(idNewNode).setBelongToHeap(true);
    						notifyNodeReached(tmp_arc.getDestination());
    					}
    				}
    			}
    		}
    	}
    	System.out.println("Nb iterations = "+nb_iterations+"\n");
    }
    
    public ShortestPathSolution findShortestPath(ShortestPathData data,	ArrayList<Label> list_label) {
    	List<Arc> shortestPath = new ArrayList<Arc>();
    	Node currentNode = data.getDestination();
    	Status status = Status.OPTIMAL;
    	int nb_arc = 0;
    	while(currentNode != data.getOrigin()) {
            
    		Arc arc_parent = list_label.get(currentNode.getId()).getFather();
    		if(arc_parent != null) {
    			shortestPath.add(arc_parent);
    			currentNode = arc_parent.getOrigin();
    			System.out.println("Node reached "+currentNode.getId()+"\n");
    			nb_arc++;
    		}
    		else {
    			currentNode=data.getOrigin();
    			status = Status.INFEASIBLE;
    		}
    	}
    	
    	// Reverse the path...
        Collections.reverse(shortestPath);
    	
    	Path pathSolution = new Path(data.getGraph(), shortestPath);
    	if(status == Status.OPTIMAL) {
    		// The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
    	}
        
        System.out.println("Nb arcs = "+nb_arc+"\n");
    	
    	ShortestPathSolution solution = new ShortestPathSolution(data,status,pathSolution);
    	return solution;
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        ArrayList<Label> list_label = Initialisation(data,tas);
        Iterations(list_label,tas, data);
        
        solution = findShortestPath(data,list_label);
        
        return solution;
    }

}
