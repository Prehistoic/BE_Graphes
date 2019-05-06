package org.insa.graph;

import org.insa.graph.Label;

public class LabelStar extends Label implements Comparable<Label> {
	
	public LabelStar(int node_id) {
		super(node_id);
	}
	
	public LabelStar(int node_id, boolean mark, float cost, Arc father) {
		super(node_id,mark,cost,father);
	}
	
	
}