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
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.Visualizer;


/**
 * 
 * @author Richard Malek
 */
public class TspAntColonyTest implements IAlgorithmListener<AntColonyEvent>
{
	//static String instance = "eil51";
	//static String instance = "kroA100";
	//static String instance = "kroA200";
	//static String instance = "pcb442";
	//static String instance = "u574";
	static String instance = "u724";
	//static String instance = "pcb1173";
	//static String instance = "u1817";
	//static String instance = "u2152";
	//static String instance = "u2319";
	//static String instance = "rl1323";
	//static String instance = "fl1400";
	//static String instance = "vm1748";
	//static String instance = "fl3795";
	//static String instance = "pcb3038";
	//static String instance = "usa13509";
	
	public static void main(String[] args)
	{
		try
		{
			//String path = "data/eil51.tsp";
			//String path = "data/d657.tsp";
           String path = "data/"+instance+".tsp";
                        
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
		for (Edge edge : graph.getEdges())
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
		double defaultPheromone = 0.01, localEvaporation = 0.65, quantumPheromone = 100;
		double alpha = 1, beta = 3;
		TspGraph graph = new TspGraph(cities);
		System.out.println("Loaded ...");
		AntBrain brain = new TspAntBrain(graph.getNodes().get(0), cities.length);
		AntColony colony = new AntColony(graph);
		colony.addAntColonyListener(this);
		colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

		Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant(brain);
		//brain.setParameters(graph.getNodeList().size(), alpha, beta);
		
		long t1 = System.currentTimeMillis();
		colony.startExploring(graph.getNodes().get(0), ants);
		long t2 = System.currentTimeMillis();
		// graph.printPheromone();
		System.out.println("Global best: " + colony.getGlobalBest());
		System.out.println("size: " + colony.getBestPath().size());
		System.out.println("nodes: " + graph.getNodes().size());
		System.out.println("time [ms]: " + (t2 - t1));
		// visualization
		Integer[] tour = new Integer[colony.getBestPath().size()];
		tour[0] = colony.getBestPath().get(0).getNode2().getID();
		for (int i = 1; i < tour.length; i++)
		{
			tour[i] = colony.getBestPath().get(i).getNode2().getID();
			if (tour[i - 1] == tour[i])
			{
				tour[i] = colony.getBestPath().get(i).getNode1().getID();
			}
		}
		int best = (int)colony.getGlobalBest();
		String path2 = "ants-"+instance+"-"+best+"-"+System.currentTimeMillis()+".png";
		Visualizer.instance().createGraph(cities, tour, path2, 800, 800);
	}

	@Override
	public void algorithmStarted(AntColonyEvent e)
	{
		System.out.println("algorithmStarted");

	}

	@Override
	public void algorithmStopped(AntColonyEvent e)
	{
		System.out.println("algorithmStopped");

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

	@Override
	public void iterationPerformed(AntColonyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
