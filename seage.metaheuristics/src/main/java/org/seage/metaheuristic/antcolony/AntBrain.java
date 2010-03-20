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
public class AntBrain {

    private static AntBrain ref;

    private AntBrain() {
    }

    public static AntBrain getBrain() {
        if (ref == null) {
            ref = new AntBrain();
            return ref;
        } else {
            return ref;
        }
    }

    /**
     * This method takes in two parameters, verts and tabu. Verts represents possible destinations
     * the ant has based on where it is, and tabu represents places the ant has already been and are off limits.
     * There will be a comparison and only the vertices the ant can visit will have their connecting nextEdges evaluated for
     * desirability. Then the random generator will be used to select the arc the ant should travel, and return that arc
     * to the ant.
     * @param verts, a list of all vertices
     * @param tabu, the list of vertices already visitedNodes
     * @return the arc the ant will travel on
     */
    public synchronized Edge calculateProbability(Vector<Node> visitedNodes, Node currentNode) {
        Vector<Edge> nextEdges = new Vector<Edge>();

        boolean same;
        System.out.println(""+currentNode.getConnectionMap().size());
        for (Edge i : currentNode.getConnectionMap()) {
            same = false;
            //System.out.println(""+visitedNodes.size());
            for (Node k : visitedNodes) {
                if (i.getDestination().getId() == k.getId()) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                nextEdges.add(i);
            }
        }

        // Loop to fill up nextEdges
		/*
         * The below will pull out the nextEdges length and weight(pheromone) to produce desirability.
         * The various arrays that are created and the subsequent call to
         * randomGen are the actual "brain".
         */

        double sum = 0;
        int selections = nextEdges.size();
        double[] distances = new double[selections];
        double[] weights = new double[selections];
        double[] working = new double[selections];
        double[] probability = new double[selections];

        for (int i = 0; i < selections; i++) {
            distances[i] = nextEdges.get(i).getEdgeLength();
            weights[i] = nextEdges.get(i).getGlobalPheromone() + nextEdges.get(i).getLocalPheromone();
        }
        for (int i = 0; i < distances.length; i++) {
            working[i] = (1 / distances[i] * .3) * (weights[i] * .8);
            sum += working[i];
        }
        for (int i = 0; i < distances.length; i++) {
            probability[i] = working[i] / sum;
        }
        Edge choice = nextEdges.get(randomGen(probability, selections));
        return choice;
    }

    /**
     * @param probability, the probability array built from possible choices
     * @return int, the integer corresponding to the index of the ACEdge object the ant should travel
     */
    private synchronized int randomGen(double[] probability, int selections) {
        Random rand = new Random();
        double spread = 1;
        int choice = 0;

        double antChoice = rand.nextDouble();

        for (int i = 0; i < selections; i++) {
            spread -= probability[i];
            if (antChoice > spread) {
                return i;
            }
        }
        return choice;
    }
}
