package org.seage.metaheuristic.antcolony;

import java.util.Vector;

/**
 *
 * @author Richard Malek
 */
public class Edge
{
	private double edgeLength;
	private String name;
	private Node originator;
	private Node destination;
	private double globalPheromone;
	private double localPheromone;
	private Vector<Node> connections = new Vector<Node>();
	
	public double getGlobalPheromone()
	{
		return globalPheromone;
	}
	
	public double getLocalPheromone()
	{
		return localPheromone;
	}
	
	public void adjustGlobalPheromone(double adjustment)
	{
		globalPheromone += adjustment;
	}
	
	public void resetLocalPheromone()
	{
		localPheromone = 0;
	}
	
	public synchronized void adjustLocalPheromone(double adjustment)
	{
		localPheromone += adjustment;
	}
	
	public Edge(Node start, Node end)
	{
		setNames(start, end);
		if (start.equals(end))
		{
			edgeLength = 0;
		}
		else
		{
			edgeLength = Node.getEdgeLength(start, end);
		}
	}
	
	private void setNames(Node start, Node end)
	{
		name = start.getName() + end.getName() + "---" + end.getName() + start.getName();
		originator = start;
		destination = end;
		connections.add(start);
		connections.add(end);
	}
	
	public Vector<Node> getConnections()
	{
		return connections;
	}
	
	/**
	 * 
	 * @return the edgeLength of this edge
	 */
	public double getEdgeLength()
	{
		return edgeLength;
	}
	
	/**
	 * @return the destination Vertice
	 */
	public Node getDestination()
	{
		return destination;
	}
	
	/**
	 * 
	 * @return the name of the originating vertice
	 */
	public Node getOriginator()
	{
		return originator;
	}
	
	/**
	 * 
	 * @return the name of this edge, made by combining the starting vertice and ending vertice
	 */
	public String getName()
	{
		return name;
	}
}
