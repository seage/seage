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
public class Graph implements java.lang.Cloneable {

    protected ArrayList<Node> _nodeList = new ArrayList<Node>();
    protected ArrayList<Edge> _edgeList = new ArrayList<Edge>();
    protected double _globalEvaporation;
    protected double _localEvaporation;
    protected int _nuberNodes;
    protected int _nuberEdges;
    protected int _numberAnts;
    private double _sum;
    private double[] _working;

    public Graph(double globEvaporCoeff, double locEvaporCoeff) {
        _globalEvaporation = globEvaporCoeff;
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

    public void calculateProbability(){
        _sum = 0;
        _working = new double[_nuberEdges];
        for (int i = 0; i < _nuberEdges; i++) {
            _working[i] = ((1 / _edgeList.get(i).getEdgeLength())) * ((_edgeList.get(i).getGlobalPheromone() + _edgeList.get(i).getLocalPheromone()));
            _sum += _working[i];
        }
        for (int i = 0; i < _nuberEdges; i++) {
            _edgeList.get(i).setProbability(_working[i] / _sum);
        }
    }

    public void printPheromone(){
        for(Edge e : getEdgeList()){
            System.out.println("global: "+e.getGlobalPheromone());
            System.out.println("local: "+e.getLocalPheromone());
        }
    }
}
