package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Richard Malek
 */

public class Ant
{
	private Node startPosition;
	private Node currentPosition;
	private double distanceTravelled;
	private Vector<Node> visited = new Vector<Node>();
	private Vector<Edge> path = new Vector<Edge>();


	public Ant(Node start)
	{
		startPosition = start;
		currentPosition = start;
		visited.add(start);
	}

	
	public Vector<Edge> explore()
	{
		int size = Graph.getInstance().getVerticeList().size();

		for (int i = 0; i < size - 1 ; i++)
		{
			Edge next = AntBrain.getBrain().calculateProbability(visited, currentPosition);
			updatePosition(next);
		}
		
		for (Edge last : Graph.getInstance().getEdgeList())
		{
			if (last.getOriginator().equals(currentPosition) || last.getDestination().equals(currentPosition))
			{
				if (last.getOriginator().equals(startPosition) || last.getDestination().equals(startPosition))
				{
					distanceTravelled += last.getEdgeLength();
					leavePheromone(last);
					path.add(last);
					return path;
				}
			}
		}
                return null;
	}
	
	private void updatePosition(Edge arcChoice)
	{
		Node choice;
		path.add(arcChoice);
		if (arcChoice.getOriginator().equals(currentPosition))
		{
			choice = (arcChoice.getDestination());
		}
		else
		{
			choice = (arcChoice.getOriginator());
		}
		distanceTravelled += arcChoice.getEdgeLength();
		leavePheromone(arcChoice);
		visited.add(choice);
		currentPosition = choice; 
	}
	
	private void leavePheromone(Edge arcChoice)
	{
		arcChoice.adjustLocalPheromone(1 / arcChoice.getEdgeLength());
	}
	
	public double getDistanceTravelled()
	{
		return distanceTravelled;
	}
	
}
