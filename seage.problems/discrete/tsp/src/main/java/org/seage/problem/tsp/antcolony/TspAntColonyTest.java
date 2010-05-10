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
        int iterations = 20, numAnts = 20;
        double defaultPheromone = 0.0001, localEvaporation = 0.25, qantumPheromone = 10;
        double alpha = 1, beta = 5;
        TspGraph graph = new TspGraph(cities, localEvaporation, defaultPheromone);
        TspAntBrain brain = new TspAntBrain(alpha, beta);
        TspAntCreator antCreator = new TspAntCreator(graph, brain, numAnts, qantumPheromone);
        AntColony colony = new AntColony(antCreator, iterations);
        colony.beginExploring();
//        graph.printPheromone();
        System.out.println("Global best: "+colony.getGlobalBest());
        System.out.println("size: " + colony.getBestPath().size());
        System.out.println("nodes: "+graph.getNodeList().size());

        // visualization
        Integer[] tour = new Integer[colony.getBestPath().size()];
        tour[0] = colony.getBestPath().get(0).getNode2().getId();
        for (int i = 1; i < tour.length; i++) {
            tour[i] = colony.getBestPath().get(i).getNode2().getId();
            if (tour[i - 1] == tour[i]) {
                tour[i] = colony.getBestPath().get(i).getNode1().getId();
            }
        }
        Visualizer.instance().createGraph(cities, tour, "ants-tour.png", 800, 800);
    }
}
