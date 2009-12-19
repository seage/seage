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
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Richard Malek
 */
public class TspSolution extends Solution {

    int[] _tour;

    public TspSolution(City[] cities) {
        initTour(cities);
    }

    private void initTour(City[] c) {
        this._tour = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = i;
        }
    }

    public int[] getTour() {
        return _tour;
    }
}
