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
    private double _localPheromone = 0;
    private double _localEvaporation = 0;
    private double _maxValuePheromone = 1;
    private double _minValuePheromone = 0.001;
    private Vector<Node> _connections = new Vector<Node>();
    private double _probability;

    public Edge(Node start, Node end, double locEvaporCoeff, int numberGraphNodes, int numberAnts) {
        _originator = start;
        _destination = end;
        _connections.add(start);
        _connections.add(end);
        _localEvaporation = locEvaporCoeff;
    }

    public double getLocalPheromone() {
        return _localPheromone;
    }

    public void setDefaultPheromone(double defaultPheromone) {
        _localPheromone = defaultPheromone;
    }

    public void addLocalPheromone(double pheromone) {
        if(pheromone > 1){
            pheromone = 1;
        }
        _localPheromone = +(_maxValuePheromone - _localPheromone)*pheromone;
    }

    public void evaporateFromEdge(){
        _localPheromone = _localPheromone*_localEvaporation;
        if(_localPheromone < _minValuePheromone){
            _localPheromone = _minValuePheromone;
        }
    }

    public double getEdgeLength() {
        return _edgeLength;
    }

    public void setEdgeLength(double length) {
        _edgeLength = length;
    }

    public double getProbability() {
        return _probability;
    }

    public void setProbability(double probability) {
        _probability = probability;
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
