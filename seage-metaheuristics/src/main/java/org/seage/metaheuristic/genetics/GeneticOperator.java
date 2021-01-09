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
package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Richard Malek (original)
 * @param <GeneType>
 */
public abstract class GeneticOperator<S extends Subject<?>> {
  protected Random _random;
  protected double _crossLengthCoef;
  protected double _mutateLengthCoef;

  public GeneticOperator() {
    _random = new Random();

    _crossLengthCoef = 0.45;
    _mutateLengthCoef = 0.3;
  }

  // / <summary>
  // / Vybira dva subjekty, kteri se budou krizit
  // / </summary>
  // / <param name="population">Aktualni populace - podle jejiz parametru se
  // vyber provadi</param>
  // / <returns>Vraci indexy vybranych subjectu</returns>
  public abstract int[] select(ArrayList<S> subjects);

  // / <summary>
  // / Krizi dva subjekty
  // / </summary>
  // / <param name="parent1">Prvni rodic</param>
  // / <param name="parent2">Druhy rodic</param>
  // / <returns>Vraci mnozinu potomku</returns>

  public abstract List<S> crossOver(S parent1, S parent2) throws Exception;

  // / <summary>
  // / Mutuje subjekt
  // / </summary>
  // / <param name="subject">Subjekt, ktery je mutovan</param>
  // / <returns>Vraci zmutovanehy Subject</returns>

  public abstract S mutate(S subject) throws Exception;

  public abstract S randomize(S subject) throws Exception;

  public void setCrossLengthCoef(double coef) {
    _crossLengthCoef = coef;
  }

  public void setMutateLengthCoef(double coef) {
    _mutateLengthCoef = coef;
  }
}
