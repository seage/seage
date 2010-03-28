/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.*;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.Visualizer;

/**
 *
 * @author Richard Malek
 */
public class TspAntColonyTest {

    public static void main(String[] args) {
        try {
            new TspAntColonyTest().run(args[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void testing(Graph graph){
        double sum = 0;
        double edges = 0;
        for (Edge edge : graph.getEdgeList()) {
            sum += edge.getEdgeLength();
            edges++;
        }
        System.out.println(sum);
        System.out.println(edges);
    }

    public void run(String path) throws Exception {
        City[] cities = CityProvider.readCities(path);
        int iterations = 1000;
        double defaultPheromone = 0.1;
        double localEvaporation = 0.98;
        int ants = (int)Math.sqrt(cities.length);
        String s = String.valueOf(1/(double)ants)+"d";
        double globalEvaporation = Math.pow(localEvaporation, Double.valueOf(s));
        TspGraph graph = new TspGraph(cities, globalEvaporation, localEvaporation, ants, defaultPheromone);
        //testing(graph);
        AntColony colony = new AntColony(ants, iterations, graph);
        colony.beginExploring();
        //graph.printPheromone();
        colony.printGlobalBest();

//        // visualization
//        Integer[] tour = new Integer[colony.getBestPath().size()];
//        for(int i=0;i<tour.length;i++)
//            tour[i] = Integer.parseInt(colony.getBestPath().get(i).getDestination().getName())-1;
//
//        Visualizer.instance().createGraph(cities, tour, "ants-tour.png", 800, 800);
    }
}
