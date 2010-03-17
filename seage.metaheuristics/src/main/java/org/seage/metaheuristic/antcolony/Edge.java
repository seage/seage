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
    private double _globalPheromone = 0;
    private double _localPheromone = 0;
    private Node _originator;
    private Node _destination;
    //private Vector<Node> _connections = new Vector<Node>();

    public Edge(Node start, Node end) {
        _originator = start;
        _destination = end;
    }

    public double getGlobalPheromone() {
        return _globalPheromone;
    }

    public double getLocalPheromone() {
        return _localPheromone;
    }

    public synchronized void addGlobalPheromone(double adjustment) {
        _globalPheromone += adjustment;
    }

    public synchronized void addLocalPheromone(double adjustment) {
        _localPheromone += adjustment;
    }

    /**
     *
     * @return the _edgeLength of this edge
     */
    public double getEdgeLength() {
        return _edgeLength;
    }

    public void setEdgeLength(double edgeLength) {
        _edgeLength = edgeLength;
    }

    /**
     * @return the destination node
     */
    public Node getDestination() {
        return _destination;
    }

    /**
     *
     * @return the originator node
     */
    public Node getOriginator() {
        return _originator;
    }
}
