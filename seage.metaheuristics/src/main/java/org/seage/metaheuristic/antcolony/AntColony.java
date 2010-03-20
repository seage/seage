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
public class AntColony {

    Graph _graph;
    private double _roundBest = Double.MAX_VALUE;
    private double _globalBest = Double.MAX_VALUE;
    private Vector<Edge> _bestPath;
    private Vector<Vector<Edge>> _reports = new Vector<Vector<Edge>>();
    private int _numAnts;
    private int _numIterations;
    private int _round = 0;
    private Random _rand = new Random();

    public AntColony(int numAnts, int numIterations, Graph graph) {
        _graph = graph;
        _numAnts = numAnts;
        _numIterations = numIterations;
    }

    public void beginExploring() {
        for (int i = 0; i < _numIterations; i++) {
            spawnAnts();
            _round++;
        }
    }

    private void solveRound() {
        double roundTotal = 0;
        for (Vector<Edge> vector : _reports) {
            if (_bestPath == null) {
                _bestPath = vector;
            }
            for (Edge edges : vector) {
                roundTotal += edges.getEdgeLength();
            }
            if (roundTotal < _roundBest) {

                _roundBest = roundTotal;
            }
            roundTotal = 0;
            if (_roundBest < _globalBest) {
                _globalBest = _roundBest;
                _bestPath = vector;
            }
        }

        System.out.println("Round " + _round);
        System.out.println("This round best was  : " + _roundBest);
        System.out.println("The global best was : " + _globalBest + "\n");

        setGlobalPheromone();

        _roundBest = Double.MAX_VALUE;
        _reports.clear();
    }

    private void spawnAnts() {
        for (int i = 0; i < _numAnts; i++) {
            Ant someAnt = new Ant(_graph, _graph.getNodeList().size() - 1);
            _reports.add(someAnt.explore());
        }
        solveRound();
    }

    private void setGlobalPheromone() {
        double limit = 0.8;
        if (_globalBest / _roundBest > limit) {
            //System.out.println(""+(_globalBest / _roundBest));
            for (Edge best : _bestPath) {
                best.addGlobalPheromone((1 / best.getEdgeLength()));
            }
        }

        for (Edge updateLocal : _graph.getEdgeList()) {
            updateLocal.addGlobalPheromone(updateLocal.getLocalPheromone());
//            updateLocal.resetLocalPheromone();
            if (!(updateLocal.getGlobalPheromone() >= 0)) {
                updateLocal.addGlobalPheromone(-(1 / updateLocal.getEdgeLength()));
            }
        }
    }
}
