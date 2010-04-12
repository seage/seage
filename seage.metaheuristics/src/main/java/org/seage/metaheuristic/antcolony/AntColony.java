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

    private double _roundBest = Double.MAX_VALUE;
    private double _globalBest = Double.MAX_VALUE;
    private Vector<Edge> _bestPath;
    private Vector<Vector<Edge>> _reports = new Vector<Vector<Edge>>();
    private int _numAnts;
    private int _numIterations;
    private Graph _graph;
    private double _pathLength;
    private double _qantumPheromone;
    private Ant _someAnt;

    public AntColony(int numAnts, int numIterations, Graph graph, double qantumPheromone, EdgeManager em) {
        _graph = graph;
        _numAnts = numAnts;
        _numIterations = numIterations;
        _qantumPheromone = qantumPheromone;
    }

    public void beginExploring() {
        for (int i = 0; i < _numIterations; i++) {
            //_graph.calculateProbability();
            Ant[] ants = new Ant[_numAnts];
            for (int j = 0; j < _numAnts; j++) {
                ants[j] = new Ant(_graph, _qantumPheromone);
                _reports.add(ants[j].explore());
            }
            solveRound();
            _graph.evaporating();

            for (int j = 0; j < _numAnts; j++)
                ants[j].leavePheromone();
        }
    }

    private void solveRound() {
        _pathLength = 0;
        for (Vector<Edge> vector : _reports) {
            if (_bestPath == null) {
                _bestPath = vector;
            }

            for (Edge edges : vector) {
                _pathLength += edges.getEdgeLength();
            }
            if (_pathLength < _roundBest) {
                _roundBest = _pathLength;
            }

            if (_roundBest < _globalBest) {
                _globalBest = _roundBest;
                _bestPath = vector;
            }
        }

//        System.out.println("Round " + _round + "\n----------------------------");
//        System.out.println("This round best was  : " + _roundBest);
//        System.out.println("The global best was : " + _globalBest + "\n");

        _roundBest = Double.MAX_VALUE;
        _reports.clear();
    }

    public Vector<Edge> getBestPath() {
        return _bestPath;
    }

    public void printGlobalBest() {
        System.out.println("Global best: " + _globalBest);
    }
}
