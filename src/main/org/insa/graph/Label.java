package org.insa.graph;

/**
 * <p>
 * Class representing the label of a summit for the Djikstra algorithm
 * </p>
 * 
 * <p>
 * A label is represented by a current summit, a mark, a cost and a father.
 * </p>
 *
 */

public class Label {
	
	// Number of the summit associated to the label
	private int current_summit;
	
	// Boolean, true when the min cost of the summit is known by the algorithm
	private boolean mark;
	
	// Current value of the shortest path from the origin to this summit
	private float cost;
	
	// Indicates the previous summit on the shortest path
	private Arc father;
	
	public Label(int node_id) {
		this.current_summit = node_id;
		this.mark = false;
		this.cost = Float.POSITIVE_INFINITY;
		this.father = null;
	}
	
	public Label(int node_id, boolean mark, float cost, Arc father) {
		this.current_summit = node_id;
		this.mark = mark;
		this.cost = cost;
		this.father = father;
	}
	
	public void setMark(boolean mark) {
		this.mark = mark;
	}
	
	public boolean getMark() {
		return this.mark;
	}
	
	public void setFather(Arc father) {
		this.father = father;
	}
	
	public Arc getFather() {
		return this.father;
	}
	
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public float getCost() {
		return this.cost;
	}
	
}