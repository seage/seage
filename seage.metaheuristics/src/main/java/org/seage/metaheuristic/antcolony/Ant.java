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

    Graph _graph;
    Node _start;
    Random _rand = new Random();
    private Node _startPosition;
    private Node _currentPosition;
    private double _distanceTravelled;
    private Vector<Node> _visited = new Vector<Node>();
    private Vector<Edge> _path = new Vector<Edge>();

    public Ant(Graph graph) {
        _graph = graph;
        _start = _graph.getNodeList().get(_rand.nextInt(_graph.getNodeList().size()));
        _startPosition = _start;
        _currentPosition = _start;
        _visited.add(_start);
    }

    public Vector<Edge> explore() {
        int size = _graph.getNodeList().size();

        for (int i = 0; i < size - 1; i++) {
            Edge next = AntBrain.getBrain().calculateProbability(_visited, _currentPosition);
            updatePosition(next);
        }

        for (Edge last : _graph.getEdgeList()) {
            if (last.getOriginator().equals(_currentPosition) || last.getDestination().equals(_currentPosition)) {
                if (last.getOriginator().equals(_startPosition) || last.getDestination().equals(_startPosition)) {
                    _distanceTravelled += last.getEdgeLength();
                    leaveLocalPheromone(last);
                    _path.add(last);
                    return _path;
                }
            }
        }
        System.out.println("null!!!");
        return null;
    }

    private void updatePosition(Edge arcChoice) {
        Node choice;
        _path.add(arcChoice);
        if (arcChoice.getOriginator().equals(_currentPosition)) {
            choice = (arcChoice.getDestination());
        } else {
            choice = (arcChoice.getOriginator());
        }
        _distanceTravelled += arcChoice.getEdgeLength();
        leaveLocalPheromone(arcChoice);
        _visited.add(choice);
        _currentPosition = choice;
    }

    private void leaveLocalPheromone(Edge arcChoice) {
        arcChoice.addLocalPheromone();
    }

    public double getDistanceTravelled() {
        return _distanceTravelled;
    }
}
