package org.insa.algo.shortestpath;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.*;
import org.insa.graph.*;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {
	
	// Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static ArrayList<Node> nodes;

    @BeforeClass
    public static void initAll() throws IOException {

        // Create nodes
        nodes = new ArrayList<Node>(5);
        for (int i = 0; i < 5; ++i) {
            nodes.add( new Node(i, null));
        }

        Node.linkNodes(nodes.get(0), nodes.get(1), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes.get(0), nodes.get(2), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes.get(0), nodes.get(4), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes.get(1), nodes.get(2), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes.get(2), nodes.get(3), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes.get(2), nodes.get(3), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes.get(2), nodes.get(3), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes.get(3), nodes.get(0), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes.get(3), nodes.get(4), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes.get(4), nodes.get(0), 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());

        graph = new Graph("ID", "", nodes, null);

    }
    
    @Test
    public void testTranspose() {
        Graph transpose = graph.transpose();

        // Basic asserts...
        assertEquals("R/" + graph.getMapId(), transpose.getMapId());
        assertEquals(graph.size(), transpose.size());
        
        ArcInspector inspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(transpose,nodes.get(0),nodes.get(4),inspector);
        ShortestPathSolution solution = null;
        
        DijkstraAlgorithm algo = new DijkstraAlgorithm(data);
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        ArrayList<Label> list_label = algo.Initialisation(data,tas);
        algo.Iterations(list_label,tas, data);
        
        solution = algo.findShortestPath(data,list_label);
        
        assertEquals(solution.getPath().isValid(),true);
        assertEquals(solution.getPath().getLength() == Path.createFastestPathFromNodes(transpose, nodes).getLength(),true);
        assertEquals(solution.getPath().getLength() == Path.createShortestPathFromNodes(transpose, nodes).getLength(),true);
    }
    
}