/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.antcolony;

import java.io.FileInputStream;
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
            sum += edge.getEdgePrice();
            edges++;
        }
        System.out.println(sum);
        System.out.println(edges);
    }

    public void run(String path) throws Exception {
        City[] cities = CityProvider.readCities(new FileInputStream(path));
        int iterations = 100, numAnts = 100;
        double defaultPheromone = 0.0001, localEvaporation = 0.95, qantumPheromone = 10;
        double alpha = 1, beta = 3;
        TspGraph graph = new TspGraph(cities, localEvaporation, defaultPheromone);
        TspAntBrain brain = new TspAntBrain();
        AntColony colony = new AntColony(brain, graph);
        colony.setParameters(numAnts, iterations, alpha, beta, qantumPheromone);
        colony.beginExploring(graph.getNodeList().get(0));
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
