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

import java.util.Vector;

/**
 *
 * @author Richard Malek (original)
 */
public class Edge {

    private double _edgeLength = 0;
    private Node _node1;
    private Node _node2;
    private double _pheromone = 0;
    private double _evaporationFactor;
    private Vector<Node> _connections = new Vector<Node>();

    public Edge(Node start, Node end, double evaporation, int numberGraphNodes, int numberAnts) {
        _node1 = start;
        _node2 = end;
        _connections.add(start);
        _connections.add(end);
        _evaporationFactor = 1 - evaporation;
    }

    public double getLocalPheromone() {
        return _pheromone;
    }

    public void setDefaultPheromone(double defaultPheromone) {
        _pheromone = defaultPheromone;
    }

    public void addLocalPheromone(double pheromone) {
        _pheromone += pheromone;
//        if (_pheromone > 1) {
//            _pheromone = 1;
//        }
    }

    public void evaporateFromEdge() {
        _pheromone = _pheromone * _evaporationFactor;
        if(_pheromone < 0.00001){
            _pheromone = 0.00001;
        }
    }

    public double getEdgeLength() {
        return _edgeLength;
    }

    public void setEdgeLength(double length) {
        _edgeLength = length;
    }

    public Node getNode2() {
        return _node2;
    }

    public Node getNode1() {
        return _node1;
    }

    public Vector<Node> getConnections() {
        return _connections;
    }
}
