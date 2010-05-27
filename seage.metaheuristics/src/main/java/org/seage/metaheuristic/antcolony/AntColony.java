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
 * @author Martin Zaloga
 */
public class AntColony {

    private double _roundBest;
    private double _globalBest;
    private Vector<Edge> _bestPath;
    private Vector<Vector<Edge>> _reports;
    private int _numAnts;
    private int _numIterations;
    private Graph _graph;
    private AntCreator _antCreator;
    private Ant[] _ants;

    public AntColony(AntCreator antCreator, int numIterations) {
        _roundBest = Double.MAX_VALUE;
        _globalBest = Double.MAX_VALUE;
        _reports = new Vector<Vector<Edge>>();
        _graph = antCreator._graph;
        _numAnts = antCreator._numAnts;
        _numIterations = numIterations;
        _antCreator = antCreator;
    }

    /**
     * Main part of ant-colony algorithm
     */
    public void beginExploring() {
        for (int i = 0; i < _numIterations; i++) {
            _ants = _antCreator.createAnts();
            for (int j = 0; j < _numAnts; j++) {
                _reports.add(_ants[j].explore());
            }
            solveRound();
            _graph.evaporating();
            for (int j = 0; j < _numAnts; j++) {
                _ants[j].leavePheromone();
            }
        }
    }

    /**
     * Evaluation for each iteration
     */
    private void solveRound() {
        double pathLength = 0;
        int counter = 0;
        for (Vector<Edge> vector : _reports) {
            if (_bestPath == null) {
                _bestPath = vector;
            }
            pathLength = _ants[counter]._distanceTravelled;
            counter++;
            if (pathLength < _roundBest) {
                _roundBest = pathLength;
            }
            if (_roundBest < _globalBest) {
                _globalBest = _roundBest;
                _bestPath = vector;
            }
        }
//        System.out.println("This round best was  : " + _roundBest);
//        System.out.println("The global best was : " + _globalBest + "\n");
        _roundBest = Double.MAX_VALUE;
        _reports.clear();
    }

    /**
     * The best solution
     * @return The best path
     */
    public Vector<Edge> getBestPath() {
        return _bestPath;
    }

    /**
     * Value finding of the best solution
     * @return - Value of the best solution
     */
    public double getGlobalBest() {
        return _globalBest;
    }
}
