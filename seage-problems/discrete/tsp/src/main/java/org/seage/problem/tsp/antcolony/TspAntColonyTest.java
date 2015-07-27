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
import org.seage.problem.tsp.TspPhenotypeEvaluator;


/**
 * 
 * @author Richard Malek
 */
public class TspAntColonyTest implements IAlgorithmListener<AntColonyEvent>
{
	private int _edges;
	
	public static void main(String[] args)
	{
		try
		{
			//String path = "data/tsp/eil51.tsp";//args[0];		// 426
        	String path = "data/tsp/berlin52.tsp";//args[0]; 	// 7542
        	//String path = "data/tsp/ch130.tsp";//args[0]; 		// 6110
        	//String path = "data/tsp/lin318.tsp";//args[0]; 		// 42029
        	//String path = "data/tsp/pcb442.tsp";//args[0]; 		// 50778
        	//String path = "data/tsp/u574.tsp";//args[0]; 		// 36905
                        
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
		_edges = cities.length * (cities.length-1)/2;
		int iterations = 5000;
//		int numAnts = 500;
//		double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone = 100;
//		double alpha = 1, beta = 3;
		int numAnts = 150;
		double defaultPheromone = 0.491, localEvaporation = 0.85, quantumPheromone = 43;
		double alpha = 1.0, beta = 2.3;
		TspGraph graph = new TspGraph(cities);
		System.out.println("Loaded ...");
		AntBrain brain = new TspAntBrain(graph);
		AntColony colony = new AntColony(graph, brain);
		colony.addAntColonyListener(this);
		colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

		Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant();
		//brain.setParameters(graph.getNodeList().size(), alpha, beta);
		
		long t1 = System.currentTimeMillis();
		colony.startExploring(graph.getNodes().get(1), ants);
		long t2 = System.currentTimeMillis();
		// graph.printPheromone();
		System.out.println("Global best: " + colony.getGlobalBest());
		System.out.println("Edges: " + colony.getBestPath().size());
		System.out.println("Nodes: " + graph.getNodes().size());
		System.out.println("Time [ms]: " + (t2 - t1));
		// visualization
		Integer[] tour = new Integer[colony.getBestPath().size()+1];
		tour[0] = colony.getBestPath().get(0).getNode1().getID();
		for (int i = 1; i < tour.length-1; i++)
		{
			tour[i] = colony.getBestPath().get(i).getNode1().getID();			
			if (i>0 &&tour[i - 1] == tour[i])
			{
				tour[i] = colony.getBestPath().get(i).getNode2().getID();
			}
			
			System.out.print(tour[i] + " ");
		}
		tour[tour.length-1] = colony.getBestPath().get(tour.length-2).getNode2().getID();
		
		System.out.println();
		System.out.println(new TspPhenotypeEvaluator(cities).evaluate(tour)[0]);
//		Arrays.sort(tour);
//		for (int i = 1; i < tour.length; i++)
//			System.out.print(tour[i] + " ");
//		System.out.println();
		
		//int best = (int)colony.getGlobalBest();
		//String path2 = "vizualization/ants-"+instance+"-"+best+"-"+System.currentTimeMillis()+".png";
		//Visualizer.instance().createGraph(cities, tour, path2, 1000, 800);
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
		System.out.println(String.format("%f - %d - %d/%d", 
				e.getAntColony().getGlobalBest(),
				e.getAntColony().getCurrentIteration(),
				e.getAntColony().getGraph().getEdges().size(),
				_edges));

	}

	@Override
	public void noChangeInValueIterationMade(AntColonyEvent e)
	{

	}

	@Override
	public void iterationPerformed(AntColonyEvent e)
	{
		//System.out.println("iterationPerformed: " + e.getAntColony().getCurrentIteration());
		//System.out.println(" - edges: " + e.getAntColony().getGraph().getEdges().size() +" / "+_edges);
		
	}
}
