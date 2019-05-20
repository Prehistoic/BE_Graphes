package org.insa.algo.shortestpath;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.*;
import org.insa.algo.AbstractSolution.Status;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.BinaryGraphReader;
import org.junit.Test;

public class testPerformance {
	
	// Small graph use for tests
	private static Graph graph;
	
	@Test
	public void doRun() throws Exception {
		ArrayList<String> mapNameTab = new ArrayList<String>();
		mapNameTab.add("/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr");
		mapNameTab.add("/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr");
		mapNameTab.add("/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/fractal.mapgr");
		
		int i,j;
		for(i=0;i<mapNameTab.size();i++) {
			String mapName = mapNameTab.get(i);
			for(j=0;j<5;j++) {
				GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
				graph = reader.read();
				
				int origine = (int)(Math.random()*graph.size());
				int destination = (int)(Math.random()*graph.size());
				
				System.out.println("Map: "+mapName+" | test n°"+j);
				testScenario(mapName,"time",origine,destination);
				testScenario(mapName,"length",origine,destination);
				System.out.println("");
			}
		}
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
    		    AStarAlgorithm algoA = new AStarAlgorithm(data);
    		    
    		    // On execute l'algorithme de Dijkstra
    		    BinaryHeap<Label> tas = new BinaryHeap<Label>();
    		    ArrayList<Label> list_label = algoD.Initialisation(data,tas);
    		    algoD.Iterations(list_label,tas, data);
    		    
    		    // On récupère les solutions
    		    ShortestPathSolution solutionD = null;
    		    ShortestPathSolution solutionA = null;
    	        solutionD = algoD.findShortestPath(data,list_label);
    	        solutionA = algoA.findShortestPath(data,list_label);
    	        
    	        // Si le path n'existe pas
    	        if(solutionD.getStatus() == Status.INFEASIBLE) {
    	        	System.out.println("No path from "+origine+" to "+destination);
    	        }
    	        // Si le path existe, on affiche le temps nécessaire aux calculs pour les deux algorithmes
    	        else {
        	        System.out.println("Path from "+origine+" to "+destination);
        	        System.out.println("Time needed for Dijkstra: "+solutionD.getSolvingTime());
        	        System.out.println("Time needed for AStar: "+solutionA.getSolvingTime());
    	        }
    		}
    	}
    }
}