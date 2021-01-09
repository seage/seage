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
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.particles;

import org.seage.metaheuristic.particles.Particle;

/**
 *
 * @author Jan Zmatlik
 */
public abstract class TspParticle extends Particle {
  public TspParticle(int dimension) {
    super(dimension);
  }

  public Integer[] getTour() {
    Integer[] tour = new Integer[getCoords().length];

    for (int i = 0; i < tour.length; i++)
      tour[i] = i;

    java.util.Arrays.sort(tour, new CoordComparator(getCoords()));

    return tour;
  }

}
