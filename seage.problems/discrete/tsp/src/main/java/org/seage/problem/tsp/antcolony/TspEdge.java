/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Edge;

public class TspEdge extends Edge {

    private double _dX;
    private double _dY;

    public TspEdge(TspNode start, TspNode end, double locEvaporCoeff, int numberGraphNodes, int numberAnts) {
        super(start, end, locEvaporCoeff, numberGraphNodes, numberAnts);
        if (start.equals(end)) {
            setEdgeLength(0);
        } else {
            setEdgeLength(calculateEdgeLength(start, end));
        }
    }

    public double calculateEdgeLength(TspNode start, TspNode end){
        _dX = (start.getX() - end.getX());
        _dY = (start.getY() - end.getY());
        return Math.sqrt(_dX*_dX + _dY*_dY);
    }
}
