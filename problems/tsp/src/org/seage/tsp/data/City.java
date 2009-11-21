package org.seage.tsp.data;

import java.io.Serializable;

/**
 * Summary description for City.
 */
public class City implements Serializable
{
	public int ID = 0;
	public double X = 0;
	public double Y = 0;

	public City(int id, Double x, Double y)
	{
		ID = id;
		X = x;
		Y = y;
	}
}
