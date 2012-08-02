/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.metaheuristic.fireflies;


/**
 *
 * @author Administrator
 */
public abstract class FireflyOperator {
        abstract public double getDistance(Solution s1, Solution s2);

    /**
     * Attracts the solution s0 to solution s1 by formula:
     * s0 = s0 + beta*s1 + r*(rand - 0.5)
     * where beta can be
     *  beta = para.initialIntensity*exp(-absorption*distance^2)
     *      or
     *  beta = para.initialIntensity/(1+absorption*distance^2) for faster computation
     * and r = para.initialRandomness if para.withDecreasingRandomness==false
     *  otherwise
     *   r = decreaseRandomness(para, iter)
     * @param s0 - solution to be attracted closer to s1
     * @param s1 - solution of attraction
     * @param para - parameters of Firefly algorithm
     * @param iter - search iteration
    */
        abstract public void attract(Solution s0, Solution s1, int iter);
        
        
    /**
     * Returns decreased step of randomness. Function is used only in a case, if
     *  para.withDecreasingRandomness == true
     * @param r - initial randomness
     * @param time
     * @return decreased randomness
     */

    public abstract Solution randomSolution();

    public abstract void randomSolution(Solution solution);

    public abstract void modifySolution(Solution solution);
}
