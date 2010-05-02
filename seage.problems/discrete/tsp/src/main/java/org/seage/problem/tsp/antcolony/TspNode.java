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
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Richard Malek (original)
 */
public class TspNode extends Node {

    private double _x;
    private double _y;

    public TspNode(int id, double x, double y) {
        super(id);
        _x = x;
        _y = y;
    }

    /**
     * Coordination X getting
     * @return x-coordination
     */
    public double getX(){
        return _x;
    }

    /**
     * Coordination Y getting
     * @return y-coordination
     */
    public double getY(){
        return _y;
    }
}