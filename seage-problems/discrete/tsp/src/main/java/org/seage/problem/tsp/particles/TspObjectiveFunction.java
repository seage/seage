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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.particles;

import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction {
  private City[] _cities;

  public TspObjectiveFunction(City[] cities) {
    _cities = cities;
  }

  @Override
  public void setObjectiveValue(Particle particle) {
    TspParticle currentParticle = ((TspParticle) particle);
    double distance = 0.0;
    Integer[] tour = currentParticle.getTour();

    try {
      distance = (int) TourProvider.getTourLenght(tour, _cities);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    particle.setEvaluation(distance);
  }
}
