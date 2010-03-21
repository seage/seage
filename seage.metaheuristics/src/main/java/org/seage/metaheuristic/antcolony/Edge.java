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
    private double _maxValuePheromone = 10;
    private double _localMultiplicator;
    private double _globalMultiplicator;
    private int _numberGraphNodes;
    private int _numberAnts;
    private Vector<Node> _connections = new Vector<Node>();

    public Edge(Node start, Node end, double globEvaporCoeff, double locEvaporCoeff, int numberGraphNodes, int numberAnts) {
        _originator = start;
        _destination = end;
        _connections.add(start);
        _connections.add(end);
        _globalEvaporation = globEvaporCoeff;
        _localEvaporation = locEvaporCoeff;
        _numberGraphNodes = numberGraphNodes;
        _numberAnts = numberAnts;
    }

    public double getGlobalPheromone() {
        return _globalPheromone;
    }

    public double getLocalPheromone() {
        return _localPheromone;
    }

    public synchronized void setDefaultPheromone(double defaultPheromone) {
        _localPheromone = defaultPheromone;
        _globalPheromone = defaultPheromone;
    }

    public void addGlobalPheromone(double pathLength) {
        _globalMultiplicator = (_numberAnts*_numberGraphNodes*_maxValuePheromone)/pathLength;
        if(_globalMultiplicator > 1){
            _globalMultiplicator = 1;
        }
        _globalPheromone = (_maxValuePheromone - _globalPheromone)*_globalMultiplicator;
    }

    public synchronized void addLocalPheromone() {
        _localMultiplicator =  (_maxValuePheromone/_edgeLength);
        if(_localMultiplicator > 1){
            _localMultiplicator = 1;
        }
        _localPheromone = (_maxValuePheromone - _localPheromone)*_localMultiplicator;
    }

    public void evaporateFromEdge(){
        _globalPheromone = _globalPheromone*_globalEvaporation;
        _localPheromone = _localPheromone*_localEvaporation;
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

    public Vector<Node> getConnections() {
        return _connections;
    }
}
