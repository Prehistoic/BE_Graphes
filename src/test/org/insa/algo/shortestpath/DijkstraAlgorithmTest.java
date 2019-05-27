package org.insa.algo.shortestpath;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.*;
import org.insa.algo.AbstractSolution.Status;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.BinaryGraphReader;
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
    public void testDoRun	() {
        int i,j;
        System.out.println("Tests sur notre propre graphe :\n");
        for(i=0;i<nodes.size();i++) {
        	for(j=0;j<nodes.size();j++) {
        		if(i!=j) {
        			ArcInspector inspector = ArcInspectorFactory.getAllFilters().get(0);
        			ShortestPathData data = new ShortestPathData(graph,nodes.get(i),nodes.get(j),inspector);
        			
        			DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
        		    BellmanFordAlgorithm algoB = new BellmanFordAlgorithm(data);
        		        
        		    // On execute l'algorithme de Dijkstra
        		    BinaryHeap<Label> tas = new BinaryHeap<Label>();
        		    ArrayList<Label> list_label = algoD.Initialisation(data,tas);
        		    algoD.Iterations(list_label,tas, data);
        		    
        		    // On récupère les solutions
        		    ShortestPathSolution solutionD = null;
        			ShortestPathSolution solutionB = null;
        	        solutionD = algoD.findShortestPath(data,list_label);
        	        solutionB = algoB.run();
        	        
        	        // Si le path n'existe pas
        	        if(solutionD.getStatus() == Status.INFEASIBLE) {
        	        	assertEquals(solutionB.getStatus() == Status.INFEASIBLE,true);
        	        	System.out.println("No path from "+i+" to "+j);
        	        }
        	        // Si le path existe, on vérifie qu'on trouve le même avec Dijkstra et BellmanFord
        	        else {
        	        	assertEquals(solutionD.getPath().isValid(),true);
            	        assertEquals(solutionD.getPath().getLength() == solutionB.getPath().getLength(), true);
            	        System.out.println("Path from "+i+" to "+j+" is valid and correct");
        	        }
        		}
        	}
        }
        System.out.println("");
    }
    
    @Test
    public void testWithMapInsa() throws Exception {
    	String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
    	System.out.println("Tests sur la carte de l'INSA :\n");
    	int origine,destination;
    	
    	System.out.println("Cas attendu: chemin nul");
    	origine = 0;
    	destination = 0;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: chemin simple");
    	origine = 527;
    	destination = 241;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: origine n'existe pas");
    	origine = -1;
    	destination = 527;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	
    	System.out.println("Cas attendu: destination n'existe pas");
    	origine = 527;
    	destination = -1;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: origine et destination n'existent pas");
    	origine = -1;
    	destination = -1;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    }
    
    @Test
    public void testWithMapHauteGaronne() throws Exception {
    	String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
    	System.out.println("Tests sur la carte de la Haute-Garonne :\n");
    	int origine,destination;
    	
    	System.out.println("Cas attendu: chemin nul");
    	origine = 0;
    	destination = 0;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: chemin simple");
    	origine = 19245;
    	destination = 83337;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: origine n'existe pas");
    	origine = -1;
    	destination = 83337;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: destination n'existe pas");
    	origine = 83337;
    	destination = -1;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    	
    	System.out.println("Cas attendu: origine et destination n'existent pas");
    	origine = -1;
    	destination = -1;
    	testScenario(mapName,"time",origine,destination);
    	testScenario(mapName,"length",origine,destination);
    	System.out.println("");
    }
    
    @Test
    public void testWithMapCarre() throws Exception {
    	String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
    	System.out.println("Tests sur le carré:\n");
    	int origine,destination;
    	
    	System.out.println("Cas attendu: chemin nul");
		origine = 0;
		destination = 0;
		testScenario(mapName,"time",origine,destination);
		testScenario(mapName,"length",origine,destination);
		System.out.println("");
		
		System.out.println("Cas attendu: chemin simple");
		origine = 13;
		destination = 21;
		testScenario(mapName,"time",origine,destination);
		testScenario(mapName,"length",origine,destination);
		System.out.println("");
		
		System.out.println("Cas attendu: origine n'existe pas");
		origine = -1;
		destination = 13;
		testScenario(mapName,"time",origine,destination);
		testScenario(mapName,"length",origine,destination);
		System.out.println("");
		
		System.out.println("Cas attendu: destination n'existe pas");
		origine = 13;
		destination = -1;
		testScenario(mapName,"time",origine,destination);
		testScenario(mapName,"length",origine,destination);
		System.out.println("");
		
		System.out.println("Cas attendu: origine et destination n'existent pas");
		origine = -1;
		destination = -1;
		testScenario(mapName,"time",origine,destination);
		testScenario(mapName,"length",origine,destination);
		System.out.println("");
    }
    
    public void testScenario(String mapName, String mode, int origine, int destination) throws Exception {
    	
    	GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    	
    	Graph graph = reader.read();
    	
    	if(origine<0 || destination<0 || origine>graph.size()-1 || destination>graph.size()-1) {
    		System.out.println("Erreur origine/destination invalide(s)");
    	}
    	else if (origine == destination) {
    		System.out.println("Chemin nul, origine = destination");
    	}
    	else {
    		ArcInspector inspector = null;
    		if(mode == "time") {
    			inspector = ArcInspectorFactory.getAllFilters().get(2);
    		}
    		else if(mode == "length") {
    			inspector = ArcInspectorFactory.getAllFilters().get(0);
    		}
    		else {
    			System.out.println("Mauvais mode !");
    		}
			
    		if(inspector !=null) {
    			ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),inspector);
    			
    			DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
    		    BellmanFordAlgorithm algoB = new BellmanFordAlgorithm(data);
    		    
    		    // On execute l'algorithme de Dijkstra
    		    BinaryHeap<Label> tas = new BinaryHeap<Label>();
    		    ArrayList<Label> list_label = algoD.Initialisation(data,tas);
    		    algoD.Iterations(list_label,tas, data);
    		    
    		    // On récupère les solutions
    		    ShortestPathSolution solutionD = null;
    			ShortestPathSolution solutionB = null;
    	        solutionD = algoD.findShortestPath(data,list_label);
    	        solutionB = algoB.run();
    	        
    	        // Si le path n'existe pas
    	        if(solutionD.getStatus() == Status.INFEASIBLE) {
    	        	assertEquals(solutionB.getStatus() == Status.INFEASIBLE,true);
    	        	System.out.println("No path from "+origine+" to "+destination);
    	        }
    	        // Si le path existe, on vérifie qu'on trouve le même avec Dijkstra et BellmanFord
    	        else {
    	        	assertEquals(solutionD.getPath().isValid(),true);
    	        	
    	        	if(mode=="time") {
    	        		assertEquals(solutionD.getPath().getMinimumTravelTime() == solutionB.getPath().getMinimumTravelTime(), true);
    	        	}
    	        	else {
    	        		assertEquals(solutionD.getPath().getLength() == solutionB.getPath().getLength(), true);
    	        	}
    	        	System.out.println("Path from "+origine+" to "+destination+" is valid and correct | Mode: "+mode+" | Temps: "+solutionD.getPath().getMinimumTravelTime()+" | Distance: "+solutionD.getPath().getLength());
      
    	        }
    		}
    	}
    }
}
