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
    private double roundBest = Double.MAX_VALUE;
    private double globalBest = Double.MAX_VALUE;
    private Vector<Edge> bestPath;
    private Vector<Vector<Edge>> reports = new Vector<Vector<Edge>>();
    private int numAnts;
    private int numIterations;
    private int round = 0;

    public AntColony(int numAnts, int numIterations, Graph graph) {
        this.numAnts = numAnts;
        this.numIterations = numIterations;
        _graph = graph;
    }

    public void beginExploring() {
        for (int i = 0; i < numIterations; i++) {
            spawnAnts();
            round++;
        }
    }

    private void solveRound() {
        double roundTotal = 0;
        for (Vector<Edge> vector : reports) {
            if (bestPath == null) {
                bestPath = vector;
            }
            for (Edge edges : vector) {
                roundTotal += edges.getEdgeLength();
            }
            if (roundTotal < roundBest) {

                roundBest = roundTotal;
            }
            roundTotal = 0;
            if (roundBest < globalBest) {
                globalBest = roundBest;
                bestPath = vector;
            }
        }

        System.out.println("Round " + round);
        System.out.println("This round best was  : " + roundBest);
        System.out.println("The global best was : " + globalBest + "\n");

        setGlobalPheromone();

//        roundBest = Double.MAX_VALUE;
        reports.clear();
    }

    private void spawnAnts() {
        for (int i = 0; i < numAnts; i++) {
            Ant someAnt = new Ant(_graph);
            reports.add(someAnt.explore());
        }
        solveRound();
    }

    private void setGlobalPheromone() {
        double limit = 0.8;
        if (globalBest / roundBest > limit) {
            //System.out.println(""+(globalBest / roundBest));
            for (Edge best : bestPath) {
                best.adjustGlobalPheromone((1 / best.getEdgeLength()));
            }
        }

//        for (Edge updateLocal : Graph.getInstance().getEdgeList()) {
//            updateLocal.adjustGlobalPheromone(updateLocal.getLocalPheromone());
//            updateLocal.resetLocalPheromone();
//            if (!(updateLocal.getGlobalPheromone() >= 0)) {
//                updateLocal.adjustGlobalPheromone(-(1 / updateLocal.getEdgeLength()));
//            }
//        }
    }
}
