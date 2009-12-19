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

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IObjectiveFunction;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Richard Malek
 */
public class TspObjectiveFunction implements IObjectiveFunction {

    private City[] _cities;

    public TspObjectiveFunction(City[] cities) {
        _cities = cities;
    }

    public double evaluateMove(Solution s, IMove m) {
        TspSolution sol = (TspSolution) s;
        TspMove move = (TspMove) m;

        int[] tour = sol.getTour();

        int ix1 = move.getIx1();
        int ix2 = move.getIx2();

        int[] tour2 = modify(tour, ix1, ix2);

        return length(tour2);
    }

    private int[] modify(int[] tour, int ix1, int ix2) {
        int pom = tour[ix1];
        tour[ix1] = tour[ix2];
        tour[ix2] = pom;
        return tour;
    }

    private double length(int[] tour) {
        double lenght = 0, x, y;
        for (int i = 0; i < tour.length; i++) {
            x = _cities[tour[i]].X;
            y = _cities[tour[i]].Y;
            lenght += Math.sqrt(x * x + y * y);
        }
        return lenght;
    }
}
