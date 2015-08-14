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
 *     Robert Harder
 *     - Initial implementation
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

/**
 * The objective function is used to
 * evaluate a {@link Solution}. 
 * 
 * @author Robert Harder
 * @version 1.0
 * @since 1.0
 */
public interface ObjectiveFunction
{
    //    public abstract double[] evaluate( Solution soln, Move move ) throws Exception;

    public abstract double[] evaluate(Solution soln) throws Exception;

    public int getCounter();

    public void incrementCounter();
} // end class ObjectiveFunction
