package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Richard Malek
 */
public class AntColony implements Observer
{
	private double roundBest = Double.MAX_VALUE;
	private double globalBest = Double.MAX_VALUE;
	private Vector<Edge> bestPath;
	private Vector<Vector<Edge>> reports = new Vector<Vector<Edge>>();
	private int numAnts;
	private int numIterations;
	private boolean nextRound = true;
	private int round;
	private boolean running = true;


	public AntColony(int numAnts, int numIterations)
	{
		this.numAnts = numAnts;
		this.numIterations = numIterations;
	}
	public void beginExploring()
	{
		round = 0;
		while (running)
		{
			while (round < numIterations)
			{
				spawnAnts();
				round++;
				nextRound = false;

			}
			if (round == numIterations)
			{
				running = false;
			}
		}
	}

	public void update(Observable o, Object arg)
	{
		reports.add((Vector<Edge>)arg);

		if (reports.size() == numAnts)
		{

//			solveRound();
		}

	}

	private void solveRound()
	{
		double roundTotal = 0d;

		for (Vector<Edge> vector : reports)
		{
                        if(bestPath == null)
                            bestPath = vector;

			for (Edge edges : vector)
			{
				roundTotal += edges.getEdgeLength();
			}
			if (roundTotal < roundBest)
			{
				roundBest = roundTotal;
			}
				
			if (roundBest < globalBest)
			{
				globalBest = roundBest;
				bestPath = vector;
			}
		}
		
		System.out.println("Round " + round + "\n----------------------------");
		System.out.println("This round best was  : " + roundBest);
		System.out.println("The global best was : " + globalBest + "\n");
		
		reinforceUpdateAndDecay();

		roundBest = Double.MAX_VALUE;
		reports.clear();
		nextRound = true;
	}

	private void spawnAnts()
	{
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < numAnts; i++)
		{
			Ant someAnt = new Ant(Graph.getInstance().getVerticeList().get(rand.nextInt(Graph.getInstance().getVerticeList().size())));
                        reports.add(someAnt.explore());
		}
                solveRound();
	}
	
	private void reinforceUpdateAndDecay()
	{
		if (globalBest/roundBest > .8 )
		{
			for (Edge best : bestPath)
			{
				best.adjustGlobalPheromone((1/best.getEdgeLength()));
			}
		}
		
		for (Edge updateLocal : Graph.getInstance().getEdgeList())
		{
			updateLocal.adjustGlobalPheromone(updateLocal.getLocalPheromone());
			updateLocal.resetLocalPheromone();
			if (!(updateLocal.getGlobalPheromone() >= 0))
			{
				updateLocal.adjustGlobalPheromone(-(1/updateLocal.getEdgeLength()));
			}
		}
	}
}
