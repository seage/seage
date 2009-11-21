package org.seage.metaheuristic.antcolony;
import java.util.*;


/**
 *
 * @author Richard Malek
 */
public class Node
{
	private String name = "";
	private double xCoordinate = 0.0;
	private double yCoordinate = 0.0;
	private ArrayList<Edge> connectionMap = new ArrayList<Edge>();
	
	public Node(String name, double xCoord, double yCoord)
	{
		this.name = name;
		this.xCoordinate = xCoord;
		this.yCoordinate = yCoord;
	}

	/** Returns the edgeLength of an edge connection two vertices
	 * 
	 * @param ACVertice alpha
	 * @param ACVertice bravo
	 * @return the edge length between alpha and bravo
	 */
	public static double getEdgeLength(Node alpha, Node bravo)
	{
		return calculateDistance(alpha, bravo);
		
	}
	
	private static double calculateDistance(Node start, Node end)
	{
		double alphaX = start.getXCoordinate();
		double alphaY = start.getYCoordinate();
		double bravoX = end.getXCoordinate();
		double bravoY = end.getYCoordinate();
		
		return Math.sqrt(Math.pow(alphaX - bravoX, 2d) + Math.pow(alphaY - bravoY, 2d));
	}


	/**
	 * @return the name of this Vertice
	 */
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the xCoordinate is returned as a double
	 */
	public double getXCoordinate()
	{
		return xCoordinate;
	}
	
	/**
	 * @return the yCoordinate is returned as a double
	 */
	public double getYCoordinate()
	{
		return yCoordinate;
	}
	
	protected void buildEdgeMap(Edge edge)
	{
		connectionMap.add(edge);
	}
	
	public ArrayList<Edge> getConnectionMap()
	{
		return connectionMap;
	}
	
}
