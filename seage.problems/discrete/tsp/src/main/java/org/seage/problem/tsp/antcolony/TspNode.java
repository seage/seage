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

    private double _x = 0;
    private double _y = 0;

    public TspNode(String id, double x, double y) {
        super(id);
        _x = x;
        _y = y;
    }

    public double getX(){
        return _x;
    }

    public double getY(){
        return _y;
    }
}