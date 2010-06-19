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
public class AntColony {

    private double _roundBest;
    private double _globalBest;
    private Vector<Edge> _bestPath;
    private Vector<Vector<Edge>> _reports;
    private Graph _graph;
    private Ant[] _ants;
    private AntBrain _antBrain;

    private int _numAnts;
    private int _numIterations;
    private double _alpha;
    private double _beta;
    private double _quantumPheromone;
    
//    private AntCreator _antCreator;
    

    public AntColony(AntBrain antBrain, Graph graph)
    {
        _antBrain = antBrain;
        _graph = graph;

        _roundBest = Double.MAX_VALUE;
        _globalBest = Double.MAX_VALUE;
        _reports = new Vector<Vector<Edge>>();    
    }

    public void setParameters(int numAnts, int numIterations, double alpha, double beta, double quantumPheromone) throws Exception
    {
        _numAnts = numAnts;
        _numIterations = numIterations;
        _alpha = alpha;
        _beta = beta;
        _quantumPheromone = quantumPheromone;
        createAnts();
    }

    /**
     * Main part of ant-colony algorithm
     */
    public void beginExploring(Node startingNode) throws Exception
    {
        for (int i = 0; i < _numIterations; i++) {            
            for (int j = 0; j < _numAnts; j++) {
                _reports.add(_ants[j].explore(startingNode));
            }
            solveRound();
            _graph.evaporating();
            for (int j = 0; j < _numAnts; j++) {
                _ants[j].leavePheromone();
            }
        }
    }

    private void createAnts() throws Exception
    {
        _ants = new Ant[_numAnts];
        _antBrain.setParameters(_graph.getNodeList().size(), _alpha, _beta);
        for(int i=0;i<_numAnts;i++)        
            _ants[i] = new Ant(_antBrain, _graph, _quantumPheromone);
        
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
