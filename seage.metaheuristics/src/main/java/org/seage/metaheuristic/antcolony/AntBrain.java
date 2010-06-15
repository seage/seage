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
public class AntBrain {

    protected double _alpha, _beta;
    private Random _rand;

    public AntBrain(double alpha, double beta) {
        _alpha = alpha;
        _beta = beta;
        _rand = new Random(System.currentTimeMillis());
    }

    /**
     * Finding available edges
     * @param currentPosition - Current position
     * @param visited - Visited nodes
     * @return - Available edges
     */
    protected List<Edge> getAvailableEdges(Node currentPosition, Vector<Node> visited) {
        return null;
    }

    /**
     * Selection following edge
     * @param edges - Available edges
     * @param visited - Visited nodes
     * @return - Selected edge
     */
    protected Edge selectNextEdge(List<Edge> edges, Vector<Node> visited) {
        double sum = 0;
        double[] probabilities = new double[edges.size()];
        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = edges.get(i);
            for (Node n : e.getConnections()) {
                if (visited.contains(n)) {
                    continue;
                } else {
                    probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgePrice(), _beta);
                    sum += probabilities[i];
                }
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return edges.get(next(probabilities));
    }

    /**
     * Next edges index calculation
     * @param probs - probabilities all edges
     * @return - Next edges index
     */
    protected int next(double[] probs) {
        double randomNumber = _rand.nextDouble();
        double numberReach;
        if (randomNumber <= 0.5) {
            numberReach = 0;
            for (int i = 0; i < probs.length; i++) {
                numberReach += probs[i];
                if (numberReach > randomNumber) {
                    return i;
                }
            }
        } else {
            numberReach = 1;
            for (int i = probs.length - 1; i >= 0; i--) {
                numberReach -= probs[i];
                if (numberReach <= randomNumber) {
                    return i;
                }
            }
        }
        return 0;
    }

    protected double pathCost(Vector<Edge> path)
    {
        return 0;
    }
}
