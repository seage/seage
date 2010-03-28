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
    protected double _globalEvaporation;
    protected double _localEvaporation;
    protected int _nuberNodes;
    protected int _numberAnts;

    public Graph(double globEvaporCoeff, double locEvaporCoeff, double defaultPheromone) {
        _globalEvaporation = globEvaporCoeff;
        _localEvaporation = locEvaporCoeff;
        setDefaultPheromone(defaultPheromone);
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

    private void setDefaultPheromone(double defaultPheromone){
        for(Edge e : getEdgeList()){
            e.setDefaultPheromone(defaultPheromone);
        }
    }

    public void printPheromone(){
        for(Edge e : getEdgeList()){
            System.out.println("global: "+e.getGlobalPheromone());
            System.out.println("local: "+e.getLocalPheromone());
        }
    }
}
