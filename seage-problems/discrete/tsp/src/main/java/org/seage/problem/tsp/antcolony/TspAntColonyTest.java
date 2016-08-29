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

import java.util.List;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.tsp.City;
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
        //City[] cities = CityProvider.readCities(new FileInputStream(path));
    	City[] cities = new City[4];
    	cities[0] = new City(1, 0.0, 0.0);
    	cities[1] = new City(2, 0.0, 1.0);
    	cities[2] = new City(3, 1.0, 0.0);
    	cities[3] = new City(4, 1.0, 1.0);
    	
        _edges = cities.length * (cities.length - 1) / 2;
        int iterations = 10000;
        //		int numAnts = 500;
        //		double defaultPheromone = 0.9, localEvaporation = 0.8, quantumPheromone = 100;
        //		double alpha = 1, beta = 3;
        int numAnts = 969;
        double defaultPheromone = 0.917556841901922, localEvaporation = 0.6269178017512955, quantumPheromone = 610.6257680691537;
        double alpha = 1.0654234316716138, beta = 1.1515958770402412;
        TspGraph graph = new TspGraph(cities);
        System.out.println("Loaded ...");
        AntBrain brain = new TspAntBrain(graph);
        AntColony colony = new AntColony(graph, brain);
        colony.addAntColonyListener(this);
        colony.setParameters(iterations, alpha, beta, quantumPheromone, defaultPheromone, localEvaporation);

        Ant ants[] = new Ant[numAnts];
        for (int i = 0; i < numAnts; i++)
            ants[i] = new Ant(null);
        //brain.setParameters(graph.getNodeList().size(), alpha, beta);

        long t1 = System.currentTimeMillis();
        colony.startExploring(graph.getNodes().get(1), ants);
        long t2 = System.currentTimeMillis();
        // graph.printPheromone();
        System.out.println("Global best: " + colony.getGlobalBest());
        System.out.println("Global best hash: " + colony.getBestPath().hashCode());
        System.out.println("Edges: " + colony.getBestPath().size());
        System.out.println("Nodes: " + graph.getNodes().size());
        System.out.println("Time [ms]: " + (t2 - t1));

        Integer[] tour = createTour(colony.getBestPath());
        for(Integer t : tour)
        	System.out.print(t+" ");
        System.out.println();
        System.out.println(new TspPhenotypeEvaluator(cities).evaluate(tour)[0]);
    }

    private Integer[] createTour(List<Edge> bestPath) 
    {
    	Integer[] tour = new Integer[bestPath.size() + 1];
    	
        tour[0] = bestPath.get(0).getNode1().getID();
        if(tour[0]!=bestPath.get(1).getNode1().getID() || tour[0]!=bestPath.get(1).getNode2().getID())
        	tour[0] = bestPath.get(0).getNode2().getID();
        for (int i = 1; i < tour.length - 1; i++)
        {
            tour[i] = bestPath.get(i).getNode1().getID();
            if (i > 0 && tour[i - 1].equals(tour[i]))
            {
                tour[i] = bestPath.get(i).getNode2().getID();
            }
        }
        tour[tour.length - 1] = tour[0];
        return tour;
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
        System.out.println(String.format("%f - %d - %d - %d/%d",
                e.getAntColony().getGlobalBest(),
                e.getAntColony().getCurrentIteration(),
                e.getAntColony().getBestPath().hashCode(),
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
