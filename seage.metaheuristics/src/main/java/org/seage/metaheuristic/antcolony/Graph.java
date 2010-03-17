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

    private ArrayList<Node> _nodeList = new ArrayList<Node>();
    private ArrayList<Edge> _edgeList = new ArrayList<Edge>();
    int _id;

    public Graph(ArrayList<Node> nodes) {
        fillNodeMap(nodes);
        fillEdgeMap();
    }

    public ArrayList<Node> getNodeList() {
        return _nodeList;
    }

    public void addNode(Node n) {
        _id = _nodeList.size();
        _nodeList.add(new Node(_id));
    }

    public void fillNodeMap(ArrayList<Node> nodes){
        for (Node n : nodes) {
            addNode(n);
        }
    }

    public ArrayList<Edge> getEdgeList() {
        return _edgeList;
    }

    public void fillEdgeMap() {
        boolean same = false; //stejne
        for (Node i : _nodeList) {
            for (Node j : _nodeList) {
                if (i.getId() != j.getId()) {
                    Edge theEdge = new Edge(i, j);
                    for (Edge k : _edgeList) {
                        if (k.getOriginator().equals(j) && k.getDestination().equals(i)) {
                            same = true;
                        }
                    }
                    if (!same) {
                        _edgeList.add(theEdge);
                    }
                }
                same = false;
            }
        }
        for (Node i : _nodeList) {
            for (Edge j : _edgeList) {
                if (j.getOriginator().equals(i) || j.getDestination().equals(i)) {
                    i.addConectionEdge(j);
                }
            }
        }
    }
}
