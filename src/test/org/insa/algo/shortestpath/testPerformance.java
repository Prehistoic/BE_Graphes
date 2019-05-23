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
			for(j=1;j<=5;j++) {
				GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
				graph = reader.read();
				
				int origine = (int)(Math.random()*graph.size());
				int destination = (int)(Math.random()*graph.size());
				
				System.out.println("Map: "+mapName+" | test n°"+j);
				testScenario(mapName,"temps",origine,destination);
				testScenario(mapName,"distance",origine,destination);
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
    		if(mode == "temps") {
    			inspector = ArcInspectorFactory.getAllFilters().get(2);
    		}
    		else if(mode == "distance") {
    			inspector = ArcInspectorFactory.getAllFilters().get(0);
    		}
    		else {
    			System.out.println("Mauvais mode !");
    		}
			
    		if(inspector !=null) {
    			ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),inspector);
    			
    			DijkstraAlgorithm algoD = new DijkstraAlgorithm(data);
    		    AStarAlgorithm algoA = new AStarAlgorithm(data);
    		    
    		    long tmpStart;
    		    long tmpEnd;
    		    
    		    // On récupère le temps de départ
    		    tmpStart=System.nanoTime();
    		    // On execute l'algorithme de Dijkstra
    		    BinaryHeap<Label> tas = new BinaryHeap<Label>();
    		    ArrayList<Label> list_label = algoD.Initialisation(data,tas);
    		    algoD.Iterations(list_label,tas, data);
    		    ShortestPathSolution solutionD = algoD.findShortestPath(data,list_label);
    		    // On récupère le temps d'arrivée
    		    tmpEnd=System.nanoTime();
    		    
    		    float tmpExecDijkstra = (tmpEnd - tmpStart)/1000000.0f;
    		    
    		    // On remet les indicateurs de temps à 0
    		    tmpStart = 0;
    		    tmpEnd = 0;
    		    
    		    // Idem pour A*
    		    tmpStart=System.nanoTime();
    		    
    		    BinaryHeap<Label> tasA = new BinaryHeap<Label>();
    		    ArrayList<Label> list_labelA = algoD.Initialisation(data,tasA);
    		    algoA.Iterations(list_labelA,tasA, data);
    		    ShortestPathSolution solutionA = algoA.findShortestPath(data,list_labelA);
    		    
    		    tmpEnd=System.nanoTime();
    		    
    		    float tmpExecAStar = (tmpEnd - tmpStart)/1000000.0f;
    	        
    	        // Si le path n'existe pas
    	        if(solutionD.getStatus() == Status.INFEASIBLE) {
    	        	System.out.println("Pas de chemin entre "+origine+" et "+destination);
    	        }
    	        // Si le path existe, on affiche le temps nécessaire aux calculs pour les deux algorithmes
    	        else {
        	        System.out.println("Chemin entre "+origine+" et "+destination+" | Mode choisi : "+mode);
        	        System.out.println("Temps pour Dijkstra: "+tmpExecDijkstra);
        	        System.out.println("Temps pour AStar: "+tmpExecAStar);
    	        }
    		}
    	}
    }
}