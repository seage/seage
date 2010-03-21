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
    private Node _originator;
    private Node _destination;
    private double _globalPheromone = 0;
    private double _localPheromone = 0;
    private double _globalEvaporation = 0;
    private double _localEvaporation = 0;
    private Vector<Node> _connections = new Vector<Node>();

    public Edge(Node start, Node end, double globEvaporCoeff, double locEvaporCoeff) {
        _originator = start;
        _destination = end;
        _connections.add(start);
        _connections.add(end);
        _globalEvaporation = globEvaporCoeff;
        _localEvaporation = locEvaporCoeff;
    }

    public double getGlobalPheromone() {
        return _globalPheromone;
    }

    public double getLocalPheromone() {
        return _localPheromone;
    }

    public void addGlobalPheromone(double addPheromone) {
        _globalPheromone += addPheromone;
    }

    public synchronized void addLocalPheromone(double addPheromone) {
        _localPheromone += addPheromone;
    }

    public void evaporateFromEdge(){
        _globalPheromone = _globalPheromone*_globalEvaporation;
        _localPheromone = _localPheromone*_localEvaporation;
    }

    public Vector<Node> getConnections() {
        return _connections;
    }

    public double getEdgeLength() {
        return _edgeLength;
    }

    public void setEdgeLength(double length) {
        _edgeLength = length;
    }

    public Node getDestination() {
        return _destination;
    }

    public Node getOriginator() {
        return _originator;
    }
}
