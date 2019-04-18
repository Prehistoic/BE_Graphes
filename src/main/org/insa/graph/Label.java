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

public class Label implements Comparable<Label> {
	
	// Number of the summit associated to the label
	private int current_summit;
	
	// Boolean, true when the min cost of the summit is known by the algorithm
	private boolean mark;
	
	// Current value of the shortest path from the origin to this summit
	private float cost;
	
	// Indicates the previous summit on the shortest path
	private Arc father;
	
	// Indicates if the label belongs to the heap
	private boolean BelongToHeap;
	
	public Label(int node_id) {
		this.current_summit = node_id;
		this.mark = false;
		this.cost = Float.POSITIVE_INFINITY;
		this.father = null;
		this.BelongToHeap = false;
	}
	
	public Label(int node_id, boolean mark, float cost, Arc father) {
		this.current_summit = node_id;
		this.mark = mark;
		this.cost = cost;
		this.father = father;
		this.BelongToHeap = false;
	}
	
	public void setBelongToHeap(boolean bool) {
		this.BelongToHeap = bool;
	}
	
	public boolean getBelongToHeap() {
		return this.BelongToHeap;
	}
	
	public int getCurrentSummit() {
		return this.current_summit;
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
	
	public int compareTo(Label l) {
		if(this.getCost() > l.getCost()) {
			return 1;
		}
		else if(this.getCost() == l.getCost()) {
			return 0;
		}
		else {
			return -1;
		}
	}
}