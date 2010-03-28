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

    private static AntBrain _ref;
    private Vector<Edge> _edges;
    private double _sum;
    private int _edgesSize;
    private double[] _working;
    private double[] _probability;
    private Random _rand;
    private double _spread;
    private double _antChoice;
    private double _randomNumber;
    private double _numberReach;

    public static AntBrain getBrain() {
        if (_ref == null) {
            _ref = new AntBrain();
            return _ref;
        } else {
            return _ref;
        }
    }

    public synchronized Edge calculateProbability(Vector<Node> visited, Node currentPosition) {
        _edges = new Vector<Edge>();
        for (Edge i : currentPosition.getConnectionMap()) {
            for (Node j : i.getConnections()) {
                if(visited.contains(j))
                    continue;
                else
                    _edges.add(i);
            }
        }

        _sum = 0;
        _edgesSize = _edges.size();
        _working = new double[_edgesSize];
        _probability = new double[_edgesSize];
        for (int i = 0; i < _edgesSize; i++) {
            _working[i] = ((1/_edges.get(i).getEdgeLength()))*((_edges.get(i).getGlobalPheromone() + _edges.get(i).getLocalPheromone()));
            _sum += _working[i];
        }
        for (int i = 0; i < _edgesSize; i++) {
            _probability[i] = _working[i] / _sum;
        }
        return _edges.get(next(_probability, _edgesSize));
    }

    /**
     * @param _probability, the _probability array built from possible choices
     * @return int, the integer corresponding to the index of the ACEdge object the ant should travel
     */
    private synchronized int next(double[] probability, int selections) {
        _rand = new Random(System.currentTimeMillis());
        _spread = 1;
        _antChoice = _rand.nextDouble();

        for (int i = 0; i < selections; i++) {
            _spread -= probability[i];
            if (_antChoice > _spread) {
                return i;
            }
        }
        return 0;
    }
//    private synchronized int next(double[] probability, int size) {
//        _rand = new Random(System.currentTimeMillis());
//        _randomNumber = _rand.nextDouble();
//        _numberReach = 0;
//        for(int i = 0; i < size-1; i++){
//            _numberReach += probability[i];
//            System.out.println("probability[i] "+probability[i]);
////            System.out.println("_numberReach " + _numberReach);
////            System.out.println("_randomNumber " + _randomNumber);
//            if(_numberReach <= _randomNumber && _randomNumber <= (_numberReach + probability[i+1])){
//                System.out.println("jo");
//                return i;
//            }
//        }
//        return 0;
//    }
}
