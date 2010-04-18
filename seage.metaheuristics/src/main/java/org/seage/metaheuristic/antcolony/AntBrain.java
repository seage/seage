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

//    protected static AntBrain _brain;
    protected Random _rand = new Random(System.currentTimeMillis());

//    public static AntBrain getBrain() {
//        if (_brain == null) {
//            _brain = new AntBrain();
//            return _brain;
//        } else {
//            return _brain;
//        }
//    }

    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        return null;
    }

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

        return probs.length - 1;
    }
}
