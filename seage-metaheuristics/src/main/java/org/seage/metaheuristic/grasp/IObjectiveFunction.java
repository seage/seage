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
package org.seage.metaheuristic.grasp;

/**
 *
 * @author Martin Zaloga
 */
public interface IObjectiveFunction
{

    /**
     * Function that returns the rating for the next step
     * @param s - Solutions for which was generated the steps
     * @param move - The next step
     * @return - Rating for the step
     */
    double evaluateMove(Solution s, IMove move) throws Exception;

    /**
     * Function that sets this to indicate that this is the first step
     */
    void reset();
}
