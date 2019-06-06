package org.insa.graph;

import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.AbstractInputData;
import org.insa.graph.Label;

public class LabelStar extends Label implements Comparable<Label> {
	
	private float costToDestination;
	private ShortestPathData data;
	
	public LabelStar(int node_id, ShortestPathData data) {
		super(node_id);
		this.data = data;
		this.costToDestination = 0;
	}
	
	public LabelStar(int node_id, boolean mark, float cost, Arc father) {
		super(node_id,mark,cost,father);
	}
	
	public float getTotalCost() {
		this.setCostToDestination();
		return this.getCost() + this.costToDestination;
	}
	
	public void setCostToDestination() {
		if(this.data.getMode() == AbstractInputData.Mode.LENGTH) {
			this.costToDestination = (float)Point.distance(this.getFather().getDestination().getPoint(),this.data.getDestination().getPoint());
		}
		else {
			int vitesse = Math.max(this.data.getMaximumSpeed(), this.data.getGraph().getGraphInformation().getMaximumSpeed());
			this.costToDestination = (float)Point.distance(this.getFather().getDestination().getPoint(),this.data.getDestination().getPoint())/(vitesse*1000.0f/3600.0f);
		}
	}

}
