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

    public void run(String path) throws Exception {
        City[] cities = CityProvider.readCities(path);
        TspGraph graph = new TspGraph(cities);

        double sum = 0;
        int edges = 0;
        for (Edge edge : graph.getEdgeList()) {
            sum += edge.getEdgeLength();
            edges++;
        }

        System.out.println(""+graph.getNodeList().size());
        System.out.println(edges);
        System.out.println(sum);

        int ants = 20;
        int iterations = 20;
        AntColony colony = new AntColony(ants, iterations, graph);
        colony.beginExploring();
    }
}
