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

//    private static AntBrain _brain;
//    private Vector<Edge> _notVisitedEdges;
    private Random _rand = new Random(System.currentTimeMillis());

//    public static AntBrain getBrain() {
//        if (_brain == null) {
//            _brain = new AntBrain();
//            return _brain;
//        } else {
//            return _brain;
//        }
//    }

    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        double alpha = 2, beta = 1;
        double sum = 0;
        double[] probabilities = new double[currentPosition.getConnectionMap().size()];

        // for each Edges
        for (int i=0;i<probabilities.length;i++) {
            Edge e = currentPosition.getConnectionMap().get(i);
            for (Node n : e.getConnections()) {
                if (visited.contains(n)) {
                    continue;
                } else {
                    probabilities[i] = Math.pow(e.getLocalPheromone(), alpha)*Math.pow(1 / e.getEdgeLength(), beta);
                    sum += probabilities[i];
                }
            }
        }
        for (int i=0;i<probabilities.length;i++)
            probabilities[i] /= sum;
        return currentPosition.getConnectionMap().get(next(probabilities));
    }

//    public void doNotVisitedList(Vector<Node> visited, Node currentPosition) {
//        _notVisitedEdges = new Vector<Edge>();
//
//    }

//    private Edge next0() {
//        double randomNumber = _rand.nextDouble();
//        double sum = 0;
//
//        Collections.sort(_notVisitedEdges, new Comparator<Edge>() {
//
//            public int compare(Edge t, Edge t1) {
//                if(t1.getProbability() > t.getProbability()) return 1;
//                if(t1.getProbability() < t.getProbability()) return -1;
//                return 0;
//            }
//        });
//
////        for (int i = 0; i <1/* _notVisitedEdges.size()/10*/; i++) {
////            sum += _notVisitedEdges.get(i).getProbability();
////            //if (randomNumber < sum) {
////                return i;
////            //}
////        }
//        return _notVisitedEdges.get(0);
//        //return _notVisitedEdges.get(_rand.nextInt(_notVisitedEdges.size()/10+1));
//    }
//
//    private Edge next1() {
//        double max = Double.MIN_VALUE;
//        int ix = 0;
//
//        for(int i=0;i<_notVisitedEdges.size();i++)
//            if(_notVisitedEdges.get(i).getProbability()>max)
//            {
//                max = _notVisitedEdges.get(i).getProbability();
//                ix =i;
//            }
//
//        return _notVisitedEdges.get(ix);
//    }
    private int next(double[] probs) {
        double randomNumber = _rand.nextDouble();
        double numberReach = 0;

        for (int i = 0; i < probs.length; i++) {
            numberReach += probs[i];
            if (numberReach > randomNumber) {
                return i;
            }
        }

        return probs.length-1;
    }
//    private int next() {
//        _rand = new Random(System.currentTimeMillis());
//        _randomNumber = _rand.nextDouble();
//        _numberReach = 0;
//        if (_numberReach <= 0.5) {
//            for (int i = 0; i < _notVisitedEdges.size(); i++) {
//                _numberReach += _notVisitedEdges.get(i).getProbability();
//                if (_numberReach > _randomNumber) {
//                    return i;
//                }
//            }
//        } else {
//            _spread = 1;
//            for (int i = 0; i < _notVisitedEdges.size(); i++) {
//                _spread -= _notVisitedEdges.get(i).getProbability();
//                if (_numberReach > _spread) {
//                    return i;
//                }
//            }
//        }
//        return 0;
//    }
}
