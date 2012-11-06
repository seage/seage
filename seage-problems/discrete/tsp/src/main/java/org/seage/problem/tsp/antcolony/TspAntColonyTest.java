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

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.*;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.Visualizer;

/**
 * 
 * @author Richard Malek
 */
public class TspAntColonyTest implements IAlgorithmListener<AntColonyEvent>
{
	public static void main(String[] args)
	{
		try
		{
			String path = "data/eil51.tsp";
			new TspAntColonyTest().run(path);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void testing(Graph graph)
	{
		double sum = 0;
		double edges = 0;
		for (Edge edge : graph.getEdgeList())
		{
			sum += edge.getEdgePrice();
			edges++;
		}
		System.out.println(sum);
		System.out.println(edges);
	}

	public void run(String path) throws Exception
	{
		City[] cities = CityProvider.readCities(new FileInputStream(path));
		int iterations = 10, numAnts = 100;
		double defaultPheromone = 0.01, localEvaporation = 0.90, quantumPheromone = 10;
		double alpha = 1, beta = 3;
		TspGraph graph = new TspGraph(cities);
		AntBrain brain = new TspAntBrain(graph.getNodeList().get(0), cities.length);
		AntColony colony = new AntColony(brain, graph);
		colony.addAntColonyListener(this);
		colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

		Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant(brain, graph);
		//brain.setParameters(graph.getNodeList().size(), alpha, beta);
		
		long t1 = System.currentTimeMillis();
		colony.startExploring(graph.getNodeList().get(0), ants);
		long t2 = System.currentTimeMillis();
		// graph.printPheromone();
		System.out.println("Global best: " + colony.getGlobalBest());
		System.out.println("size: " + colony.getBestPath().size());
		System.out.println("nodes: " + graph.getNodeList().size());
		System.out.println("time [ms]: " + (t2 - t1));
		// visualization
		Integer[] tour = new Integer[colony.getBestPath().size()];
		tour[0] = colony.getBestPath().get(0).getNode2().getId();
		for (int i = 1; i < tour.length; i++)
		{
			tour[i] = colony.getBestPath().get(i).getNode2().getId();
			if (tour[i - 1] == tour[i])
			{
				tour[i] = colony.getBestPath().get(i).getNode1().getId();
			}
		}
		//Visualizer.instance().createGraph(cities, tour, "ants-tour.png", 800, 800);
	}

	@Override
	public void algorithmStarted(AntColonyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void algorithmStopped(AntColonyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void newBestSolutionFound(AntColonyEvent e)
	{
		System.out.println("new best: "+ e.getAntColony().getCurrentIteration()+" - " + e.getAntColony().getGlobalBest());

	}

	@Override
	public void noChangeInValueIterationMade(AntColonyEvent e)
	{
		// TODO Auto-generated method stub

	}
}
