package org.insa.graph;

import org.insa.graph.Label;

public class LabelStar extends Label implements Comparable<Label> {
	
	public Node destination;
	
	public LabelStar(int node_id, Node destination) {
		super(node_id);
		this.destination=destination;
	}
	
	public LabelStar(int node_id, boolean mark, float cost, Arc father) {
		super(node_id,mark,cost,father);
	}
	
	public float getTotalCost() {
		float costToOrigin = this.getCost();
		double costToDestination=Point.distance(this.getFather().getDestination().getPoint(),this.destination.getPoint());
		return costToOrigin + (float)costToDestination;
	}

}