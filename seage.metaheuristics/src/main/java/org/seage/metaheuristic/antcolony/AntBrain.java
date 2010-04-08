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

    private static AntBrain _brain;
    private Vector<Edge> _notVisitedEdges;
//    private Random _rand;
//    private double _randomNumber;
//    private double _numberReach;
//    double _spread;

    public static AntBrain getBrain() {
        if (_brain == null) {
            _brain = new AntBrain();
            return _brain;
        } else {
            return _brain;
        }
    }

    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        doNotVisitedList(visited, currentPosition);
        return _notVisitedEdges.get(next());
    }

    public void doNotVisitedList(Vector<Node> visited, Node currentPosition) {
        _notVisitedEdges = new Vector<Edge>();
        for (Edge i : currentPosition.getConnectionMap()) {
            for (Node j : i.getConnections()) {
                if (visited.contains(j)) {
                    continue;
                } else {
                    _notVisitedEdges.add(i);
                }
            }
        }
    }

    private int next() {
        double probability = 0;
        double bestProbability = 0;
        int index = 0;
        for (int i = 0; i < _notVisitedEdges.size(); i++) {
            probability = _notVisitedEdges.get(i).getProbability();
            if(bestProbability < probability){
                bestProbability = probability;
                index = i;
            }
        }
        return index;
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
