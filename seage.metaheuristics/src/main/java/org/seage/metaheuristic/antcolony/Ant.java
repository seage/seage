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
public class Ant {

    private Graph _graph;
    private Node _start;
    private Random _rand = new Random();
    private Node _startPosition;
    private Node _currentPosition;
    private Node _originator;
    private Node _destination;
    private double _distanceTravelled;
    private Vector<Node> _visited = new Vector<Node>();
    private Vector<Edge> _path = new Vector<Edge>();
    Node _choiceNode;

    public Ant(Graph graph) {
        _graph = graph;
        _start = _graph.getNodeList().get(_rand.nextInt(_graph.getNodeList().size()));
        _startPosition = _start;
        _currentPosition = _start;
        _visited.add(_start);
    }

    public Vector<Edge> explore() {
        for (int i = 0; i < _graph.getNodeList().size() - 1; i++) {
            updatePosition(AntBrain.getBrain().calculateProbability(_visited, _currentPosition));
        }
        for (Edge last : _graph.getEdgeList()) {
            _originator = last.getOriginator();
            _destination = last.getDestination();
            if (_originator.equals(_currentPosition) || _destination.equals(_currentPosition)) {
                if (_originator.equals(_startPosition) || _destination.equals(_startPosition)) {
                    _distanceTravelled += last.getEdgeLength();
                    _path.add(last);
                    return _path;
                }
            }
        }
        return null;
    }

    private void updatePosition(Edge arcChoice) {
        _path.add(arcChoice);
        if (arcChoice.getOriginator().equals(_currentPosition)) {
            _choiceNode = (arcChoice.getDestination());
        } else {
            _choiceNode = (arcChoice.getOriginator());
        }
        _distanceTravelled += arcChoice.getEdgeLength();
        leaveLocalPheromone(arcChoice);
        _visited.add(_choiceNode);
        _currentPosition = _choiceNode;
    }

    private void leaveLocalPheromone(Edge arcChoice) {
        arcChoice.addLocalPheromone();
    }

    public double getDistanceTravelled() {
        return _distanceTravelled;
    }
}
