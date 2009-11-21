/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Richard Malek (original)
 */
public class Graph
{
	private static Graph ref;

	private ArrayList<Node> verticeList = new ArrayList<Node>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();

	private Graph()
	{
//		fillVerticeMap();
//		fillEdgeMap();
	}

	public static Graph getInstance()
	{
		if (ref == null)
		{
			ref = new Graph();
			return ref;
		}
		else
		{
			return ref;
		}

	}

	public ArrayList<Node> getVerticeList()
	{
		return verticeList;
	}

        public void addVertice(double x, double y)
        {
            String id = new Integer(verticeList.size()+1).toString();
            verticeList.add(new Node(id, x, y));
        }

	public ArrayList<Edge> getEdgeList()
	{
		return edgeList;
	}

	public void fillEdgeMap()
	{
		boolean same = false;

		for (Node i: verticeList)
		{
			for (Node j : verticeList)
			{
				if (! i.equals(j))
				{
					Edge theEdge = new Edge(i,j);
					for (Edge k : edgeList)
					{
						if (k.getOriginator().equals(j) && k.getDestination().equals(i))
						{
							same = true;
						}
					}
					if (!same)
					{
						edgeList.add(theEdge);
					}
				}
				same = false;
			}
		}

		for (Node i : verticeList)
		{
			for (Edge j : edgeList)
			{
				if (j.getOriginator().equals(i) || j.getDestination().equals(i))
				{
					i.buildEdgeMap(j);
				}
			}
		}
	}
}
