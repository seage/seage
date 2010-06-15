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
 *     Martin Zaloga
 *     - Reimplementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

    protected Graph _graph;
    protected Node _startPosition;
    protected Node _currentPosition;
    protected double _distanceTravelled;
    protected Vector<Node> _visited;
    protected Vector<Edge> _path;
    protected AntBrain _brain;
    private double _qantumPheromone;

    public Ant(Graph graph, double qantumPheromone, AntBrain brain) {
        _graph = graph;
        _brain = brain;
        _qantumPheromone = qantumPheromone;
        _visited = new Vector<Node>();
        _path = new Vector<Edge>();
    }

    /**
     * Ant passage through the graph
     * @return - ants path
     */
    protected Vector<Edge> explore() {
        List<Edge> edges = _brain.getAvailableEdges(_currentPosition, _visited);
        while (edges != null) {
            Edge nextEdge = _brain.selectNextEdge(edges, _visited);
            updatePosition(nextEdge);
            edges = _brain.getAvailableEdges(_currentPosition, _visited);
        }        

        _distanceTravelled = _brain.pathCost(_path);
        leavePheromone();
        return _path; // Report
    }

    /**
     * Update ants position
     * @param selectedEdge - Actual selected edge
     */
    protected void updatePosition(Edge arcChoice) {
        Node choiceNode;
        if (arcChoice.getNode1().equals(_currentPosition)) {
            choiceNode = (arcChoice.getNode2());
        } else {
            choiceNode = (arcChoice.getNode1());
        }
        _distanceTravelled += arcChoice.getEdgePrice();
        _path.add(arcChoice);
        _visited.add(choiceNode);
        _currentPosition = choiceNode;
    }

    /**
     * Pheromone leaving
     */
    protected void leavePheromone() {
        for (Edge edge : _path) {
            edge.addLocalPheromone(_qantumPheromone / (_distanceTravelled));
        }
    }
}
