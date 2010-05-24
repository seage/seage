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

    private double _edgePrice;
    private Node _node1;
    private Node _node2;
    private double _pheromone;
    private double _evaporationFactor;
    private Vector<Node> _connections;

    public Edge(Node start, Node end, double evaporation) {
        _edgePrice = 0;
        _node1 = start;
        _node2 = end;
        _pheromone = 0;
        _connections = new Vector<Node>();
        _connections.add(start);
        _connections.add(end);
        _evaporationFactor = 1 - evaporation;
    }

    /**
     * Pheromone on edge finding
     * @return - Value of pheromone
     */
    public double getLocalPheromone() {
        return _pheromone;
    }

    /**
     * Setting default pheromone
     * @param defaultPheromone - Value of default pheromone
     */
    public void setDefaultPheromone(double defaultPheromone) {
        _pheromone = defaultPheromone;
    }

    /**
     * Local pheromone addition
     * @param pheromone
     */
    public void addLocalPheromone(double pheromone) {
        _pheromone += pheromone;
    }

    /**
     * Pheromone evaporation
     */
    public void evaporateFromEdge() {
        _pheromone = _pheromone * _evaporationFactor;
        if(_pheromone < 0.00001){
            _pheromone = 0.00001;
        }
    }

    /**
     * Edge length finding
     * @return - Edge length
     */
    public double getEdgePrice() {
        return _edgePrice;
    }

    /**
     * Edge length setting
     * @param length - Edge length
     */
    public void setEdgePrice(double length) {
        _edgePrice = length;
    }

    /**
     * Firs of nodes
     * @return - First node
     */
    public Node getNode2() {
        return _node2;
    }

    /**
     * Second of nodes
     * @return - Second node
     */
    public Node getNode1() {
        return _node1;
    }

    /**
     * Both nodes
     * @return - Both nodes
     */
    public Vector<Node> getConnections() {
        return _connections;
    }
}
