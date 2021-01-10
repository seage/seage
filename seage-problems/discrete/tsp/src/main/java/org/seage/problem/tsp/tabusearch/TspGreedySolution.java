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
package org.seage.problem.tsp.tabusearch;

import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Richard Malek
 */

public class TspGreedySolution extends TspSolution {
  public TspGreedySolution(City[] cities) throws Exception {
    super(cities);
    _tour = TourProvider.createGreedyTour(cities, System.currentTimeMillis());
  }

} // end class TspGreedyStartSolution
