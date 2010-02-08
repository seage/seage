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
package org.seage.problem.sat.hillclimber;

import java.util.Random;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatSolution extends Solution {

    /**
     * _rnd - Variable for the purposes of generating the random numbers
     */
    protected Literal[] _literals;
    protected Random _rnd;

    /**
     * Constructor the solution with using the random algorithm for initial solution
     */
    public SatSolution() {
        _rnd = new Random();
    }

    public Literal[] getTour() {
        return _literals;
    }

    public void setTour(Literal[] clauses) {
        _literals = clauses;
    }
}