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

    public void testing(Graph graph) {
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
        int iterations = 100;
        double defaultPheromone = 0.0001;
        double localEvaporation = 0.5;
        double qantumPheromone = 1;
        int ants = 50;//(int)Math.sqrt(cities.length);
        TspGraph graph = new TspGraph(cities, localEvaporation, ants, defaultPheromone);
        //testing(graph);
//        graph.printPheromone();
        AntColony colony = new AntColony(ants, iterations, graph, qantumPheromone);
        colony.beginExploring();
        graph.printPheromone();
        colony.printGlobalBest();
        System.out.println("size: " + colony.getBestPath().size());

        // visualization
        Integer[] tour = new Integer[colony.getBestPath().size()];
        tour[0] = Integer.parseInt(colony.getBestPath().get(0).getDestination().getName()) - 1;
        for (int i = 1; i < tour.length; i++) {
            tour[i] = Integer.parseInt(colony.getBestPath().get(i).getDestination().getName()) - 1;
            if(tour[i-1] == tour[i]){
                tour[i] = Integer.parseInt(colony.getBestPath().get(i).getOriginator().getName()) - 1;
            }
        }
        Visualizer.instance().createGraph(cities, tour, "ants-tour.png", 800, 800);
    }
}
