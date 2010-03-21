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
	protected ArrayList<Node> _verticeList = new ArrayList<Node>();
	protected ArrayList<Edge> _edgeList = new ArrayList<Edge>();

	public Graph()
	{
//		fillVerticeMap();
//		fillEdgeMap();
	}

	public ArrayList<Node> getVerticeList()
	{
		return _verticeList;
	}

	public ArrayList<Edge> getEdgeList()
	{
		return _edgeList;
	}
}
