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
 * @deprecated Replaced by TspProblemSolver
 */
public class TspAntsMain
{
	public static void main(String[] args)
	{
            try
            {
		new TspAntsMain().run(args[0]);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
	}

        public void run(String path) throws Exception
        {
            City[] cities = CityProvider.readCities(path);

            for(City c : cities)
                Graph.getInstance().addVertice(c.X, c.Y);
            Graph.getInstance().fillEdgeMap();

            double sum = 0;
            double edges = 0;
            for (Edge edge : Graph.getInstance().getEdgeList())
            {
                    sum += edge.getEdgeLength();
                    edges++;
            }

            System.out.println(sum);
            System.out.println(edges);

            int ants = 20;
            int iterations = 100;
            AntColony brain = new AntColony(ants, iterations);
            brain.beginExploring();
        }
}
