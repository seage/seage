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
    private Random _rand = new Random(hashCode());
    private Node _startPosition;
    private Node _currentPosition;
    private double _distanceTravelled;
    private double _qantumPheromone;
    private Vector<Node> _visited = new Vector<Node>();
    private Vector<Edge> _path = new Vector<Edge>();
    private AntBrain _brain;

    public Ant(Graph graph, double qantumPheromone, AntBrain brain) {
        _graph = graph;
        _brain = brain;
        Node start = _graph.getNodeList().get(_rand.nextInt(_graph.getNodeList().size()));
        _startPosition = start;
        _currentPosition = start;
        _visited.add(start);
        _qantumPheromone = qantumPheromone;
    }

    public Vector<Edge> explore() {
        for (int i = 0; i < _graph.getNodeList().size() - 1; i++) {
            updatePosition(_brain.getNextEdge(_visited, _currentPosition));
        }
        for (Edge last : _graph.getEdgeList()) {
            Node node1 = last.getNode1();
            Node node2 = last.getNode2();
            if (node1.equals(_currentPosition) || node2.equals(_currentPosition)) {
                if (node1.equals(_startPosition) || node2.equals(_startPosition)) {
                    _distanceTravelled += last.getEdgeLength();
                    _path.add(last);
                    return _path;
                }
            }
        }        
        return _path;
    }

    private void updatePosition(Edge arcChoice) {
        _path.add(arcChoice);
        Node choiceNode;
        if (arcChoice.getNode1().equals(_currentPosition)) {
            choiceNode = (arcChoice.getNode2());
        } else {
            choiceNode = (arcChoice.getNode1());
        }
        _distanceTravelled += arcChoice.getEdgeLength();
        _visited.add(choiceNode);
        _currentPosition = choiceNode;
    }

    public void leavePheromone() {
        for (Edge edge : _path) {
            edge.addLocalPheromone(_qantumPheromone/_distanceTravelled);
        }
    }
}