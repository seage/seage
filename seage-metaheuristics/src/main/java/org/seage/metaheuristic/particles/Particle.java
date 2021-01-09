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

package org.seage.metaheuristic.particles;

import java.util.Comparator;

/**
 *
 * @author Jan Zmatlik
 */
public class Particle implements Cloneable {

  protected double[] _coords;

  protected double[] _velocity;

  private double _evaluation = Double.MAX_VALUE;

  public Particle(int dimension) {
    _coords = new double[dimension];
    _velocity = new double[dimension];
  }

  public double[] getCoords() {
    return _coords;
  }

  public void setCoords(double[] coords) {
    this._coords = coords;
  }

  public double getEvaluation() {
    return _evaluation;
  }

  public void setEvaluation(double evaluation) {
    this._evaluation = evaluation;
  }

  public double[] getVelocity() {
    return _velocity;
  }

  public void setVelocity(double[] velocity) {
    this._velocity = velocity;
  }

  @Override
  public Particle clone() {
    try {
      Particle particle = (Particle) super.clone();
      particle._coords = _coords.clone();
      particle._velocity = _velocity.clone();
      particle._evaluation = _evaluation;
      return particle;
    } catch (CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }
  }

  protected class CoordComparator implements Comparator<Integer> {
    double _coords[];

    public CoordComparator(double[] coords) {
      _coords = coords;
    }

    @Override
    public int compare(Integer t, Integer t1) {
      if (_coords[t] > _coords[t1])
        return 1;
      if (_coords[t] < _coords[t1])
        return -1;

      return 0;
    }

  }
}
