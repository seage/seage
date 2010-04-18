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

    protected ArrayList<Node> _nodeList = new ArrayList<Node>();
    protected ArrayList<Edge> _edgeList = new ArrayList<Edge>();
    protected double _localEvaporation;
    protected int _nuberNodes;
    protected int _nuberEdges;

    public Graph(double locEvaporCoeff) {
        _localEvaporation = locEvaporCoeff;
    }

    public ArrayList<Node> getNodeList() {
        return _nodeList;
    }

    public ArrayList<Edge> getEdgeList() {
        return _edgeList;
    }

    public void evaporating(){
        for(Edge e : getEdgeList()){
            e.evaporateFromEdge();
        }
    }

    protected void setDefaultPheromone(double defaultPheromone){
        for(Edge e : getEdgeList()){
            e.setDefaultPheromone(defaultPheromone);
        }
    }

    public void printPheromone(){
        for(Node n : _nodeList)
        {
            System.out.println(n.getId());
            for(Edge e : n.getConnectionMap()){
                System.out.printf("\t%3.3f\t%3.5f\n",e.getEdgeLength(),e.getLocalPheromone());
            }
        }
    }
}
