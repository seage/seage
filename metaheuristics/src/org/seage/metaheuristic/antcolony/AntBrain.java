package org.seage.metaheuristic.antcolony;

import java.util.*;


/**
 *
 * @author Richard Malek
 */
public class AntBrain
{
	private static AntBrain ref;
	private AntBrain(){}
	
	public static AntBrain getBrain()
	{
		if (ref == null)
		{
			ref = new AntBrain();
			return ref;
		}
		else
		{
			return ref;
		}
	}
	/**
	 * This method takes in two parameters, verts and tabu. Verts represents possible destinations
	 * the ant has based on where it is, and tabu represents places the ant has already been and are off limits.
	 * There will be a comparison and only the vertices the ant can visit will have their connecting arcs evaluated for
	 * desirability. Then the random generator will be used to select the arc the ant should travel, and return that arc
	 * to the ant.
	 * @param verts, a list of all vertices
	 * @param tabu, the list of vertices already visited
	 * @return the arc the ant will travel on
	 */
	
	public synchronized Edge calculateProbability(Vector<Node> visited, Node currentPosition)
	{
		Vector<Edge> arcs = new Vector<Edge>();
		
		boolean same;
		
		for (Edge i : currentPosition.getConnectionMap())
		{
			for (Node j : i.getConnections())
			{
				same = false;
				for (Node k : visited)
				{
					if (j.getName().equals(k.getName()))
					{
						same = true;
						break;
					}
				}
				if (!same)
				{
				arcs.add(i);
				}
			}
		}


		// Loop to fill up arcs
		/*
		 * The below will pull out the arcs length and weight(pheromone) to produce desirability.
		 * The various arrays that are created and the subsequent call to 
		 * randomGen are the actual "brain".
		 */
		
		double sum = 0;
		int selections = arcs.size();
		double[] distances = new double[selections];
		double[] weights = new double[selections];
		double[] working = new double[selections];
		double[] probability = new double[selections];
		

		for (int i = 0; i < selections; i++)
		{
			distances[i] = arcs.get(i).getEdgeLength();
			weights[i] = arcs.get(i).getGlobalPheromone() + arcs.get(i).getLocalPheromone();
		}

		for (int i = 0; i < distances.length; i++)
		{
			working[i] = (1/distances[i] * .3) * (weights[i] * .8);
			sum += working[i];
		}
		for (int i = 0; i < distances.length; i++)
		{
			probability[i] = working[i] / sum;
		}
		Edge choice = arcs.get(randomGen(probability, selections));
		return choice;
	}



	/**
	 * @param probability, the probability array built from possible choices
	 * @return int, the integer corresponding to the index of the ACEdge object the ant should travel
	 */
	private synchronized int randomGen(double[] probability, int selections)
	{
		Random rand = new Random(System.currentTimeMillis());
		double spread = 1;
		int choice = 0;

		double antChoice = rand.nextDouble();

		for (int i = 0; i < selections; i++)
		{
			spread -= probability[i];
			if (antChoice > spread)
			{
				return i;
			}
		}
		return choice;
	}
}
