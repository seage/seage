package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.*;
import org.seage.problem.tsp.data.City;
import org.seage.problem.tsp.data.CityProvider;

/**
 * AntColony.java Jun 20, 2009
 * Copyright @author Nathan (erewnoh) erewnoh@mac.com
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
