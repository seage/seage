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
public class Graph {

    protected ArrayList<Node> _nodeList;
    protected ArrayList<Edge> _edgeList;
    protected double _localEvaporation;
    protected int _nuberNodes;
    protected int _nuberEdges;

    public Graph(double locEvaporCoeff) {
        _nodeList = new ArrayList<Node>();
        _edgeList = new ArrayList<Edge>();
        _localEvaporation = locEvaporCoeff;
    }

    /**
     * List of nodes of graph
     * @return - List of nodes
     */
    public ArrayList<Node> getNodeList() {
        return _nodeList;
    }

    /**
     * List of edges of graph
     * @return - List of edges
     */
    public ArrayList<Edge> getEdgeList() {
        return _edgeList;
    }

    /**
     * Evaporating from each edges of graph
     */
    public void evaporating() {
        for (Edge e : getEdgeList()) {
            e.evaporateFromEdge();
        }
    }

    /**
     * Default pheromone setting for all edges
     * @param defaultPheromone - Default pheromone
     */
    protected void setDefaultPheromone(double defaultPheromone) {
        for (Edge e : getEdgeList()) {
            e.setDefaultPheromone(defaultPheromone);
        }
    }

    /**
     * Pheromone printing
     */
    public void printPheromone() {
    }
}
