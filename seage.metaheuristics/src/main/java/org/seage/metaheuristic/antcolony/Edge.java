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

    private double _edgeLength;
    private String _name;
    private Node _originator;
    private Node _destination;
    private double _globalPheromone;
    private double _localPheromone;
    private Vector<Node> _connections = new Vector<Node>();

    public double getGlobalPheromone() {
        return _globalPheromone;
    }

    public double getLocalPheromone() {
        return _localPheromone;
    }

    public void adjustGlobalPheromone(double adjustment) {
        _globalPheromone += adjustment;
    }

    public void resetLocalPheromone() {
        _localPheromone = 0;
    }

    public synchronized void adjustLocalPheromone(double adjustment) {
        _localPheromone += adjustment;
    }

    public Edge(Node start, Node end) {
        setNames(start, end);
    }

    private void setNames(Node start, Node end) {
        _name = start.getName() + end.getName() + "---" + end.getName() + start.getName();
        _originator = start;
        _destination = end;
        _connections.add(start);
        _connections.add(end);
    }

    public Vector<Node> getConnections() {
        return _connections;
    }

    /**
     *
     * @return the _edgeLength of this edge
     */
    public double getEdgeLength() {
        return _edgeLength;
    }

    public void setEdgeLength(double length) {
        _edgeLength = length;
    }

    /**
     * @return the _destination Vertice
     */
    public Node getDestination() {
        return _destination;
    }

    /**
     *
     * @return the _name of the originating vertice
     */
    public Node getOriginator() {
        return _originator;
    }

    /**
     *
     * @return the _name of this edge, made by combining the starting vertice and ending vertice
     */
    public String getName() {
        return _name;
    }
}
