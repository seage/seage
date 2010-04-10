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
    protected double _localEvaporation;
    protected int _nuberNodes;
    protected int _nuberEdges;
    protected int _numberAnts;

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

//    public void calculateProbability(){
//        double alpha = 4, beta = 1;
//
//        for(Node n : _nodeList)
//        {
//            double[] working = new double[n.getConnectionMap().size()];
//            double sum = 0;
//
//            for (int i = 0; i < n.getConnectionMap().size(); i++)
//            {
//                Edge e = n.getConnectionMap().get(i);
//                working[i] = Math.pow(e.getLocalPheromone(), alpha)*Math.pow(1 / e.getEdgeLength(), beta);
//                sum += working[i];
//            }
//            for (int i = 0; i < n.getConnectionMap().size(); i++) {
//                n.getConnectionMap().get(i).setProbability(working[i] / sum);
//            }
//        }
//    }

    public void printPheromone(){
        for(Node n : _nodeList)
        {
            System.out.println(n.getName());
            for(Edge e : n.getConnectionMap()){
                System.out.printf("\t%3.3f\t%3.5f\n",e.getEdgeLength(),e.getLocalPheromone());
                //System.out.println("\t"+e.getEdgeLength()+"\t"+e.getLocalPheromone()+"\t"+e.getProbability());
            }
        }
    }
}
