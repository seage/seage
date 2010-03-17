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
    private Node startPosition;
    private Node currentPosition;
    private double distanceTravelled;
    private Vector<Node> visited = new Vector<Node>();
    private Vector<Edge> path = new Vector<Edge>();

    public Ant(Graph graph) {
        System.out.println(""+graph);
        _start = _graph.getNodeList().get(_rand.nextInt(_graph.getNodeList().size()));
        startPosition = _start;
        currentPosition = _start;
        visited.add(_start);
        _graph = graph;
    }

    public Vector<Edge> explore() {
        int size = _graph.getNodeList().size();

        for (int i = 0; i < size - 1; i++) {
            Edge next = AntBrain.getBrain().calculateProbability(visited, currentPosition);
            updatePosition(next);
        }

        for (Edge last : _graph.getEdgeList()) {
            if (last.getOriginator().equals(currentPosition) || last.getDestination().equals(currentPosition)) {
                if (last.getOriginator().equals(startPosition) || last.getDestination().equals(startPosition)) {
                    distanceTravelled += last.getEdgeLength();
                    leavePheromone(last);
                    path.add(last);
                    return path;
                }
            }
        }
        System.out.println("null!!!");
        return null;
    }

    private void updatePosition(Edge arcChoice) {
        Node choice;
        path.add(arcChoice);
        if (arcChoice.getOriginator().equals(currentPosition)) {
            choice = (arcChoice.getDestination());
        } else {
            choice = (arcChoice.getOriginator());
        }
        distanceTravelled += arcChoice.getEdgeLength();
        leavePheromone(arcChoice);
        visited.add(choice);
        currentPosition = choice;
    }

    private void leavePheromone(Edge arcChoice) {
        arcChoice.addLocalPheromone(1 / arcChoice.getEdgeLength());
       //System.out.println(""+arcChoice.getLocalPheromone());
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }
}
