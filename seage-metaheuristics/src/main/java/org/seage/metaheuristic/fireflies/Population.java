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
 *     Richard Malek
 *     - Initial implementation
 *     Karel Durkota
 */

package org.seage.metaheuristic.fireflies;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karel Durkota
 */
public class Population<S extends Solution> {
  private ArrayList<S> population;

  public Population() {
    population = new ArrayList<S>();
  }

  public void addSolution(S solution) {
    population.add(solution);
  }

  public S getSolution(int ix) throws Exception {
    try {
      return population.get(ix);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void removeAll() {
    population.clear();
  }

  public int getSize() {
    return population.size();
  }

  public List<S> getList() {
    return population;
  }

  public void removeTwins() throws Exception {
    try {
      ArrayList<S> newPopulation = new ArrayList<S>();
      newPopulation.add(getSolution(0));
      for (int i = 1; i < population.size(); i++) {
        S prev = population.get(i - 1);
        S curr = population.get(i);
        if (curr.hashCode() != prev.hashCode()) {
          newPopulation.add(curr);
        }
      }
      population.clear();
      population.addAll(newPopulation);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void resize(int newLength) {
    if (getSize() > newLength) {
      population = new ArrayList<S>(population.subList(0, newLength));
    }
  }

  @SuppressWarnings("unchecked")
  public S getBestSolution() throws CloneNotSupportedException {
    return (S) population.get(0).clone();
  }

  public void mergePopulation(Population<S> population2) {
    population.addAll(population2.population);
  }

  public List<S> getSolutions() throws Exception {
    return getSolutions(population.size());
  }

  public List<S> getSolutions(int numSubjects) throws Exception {
    try {
      int length = population.size();
      if (length > numSubjects) {
        return population.subList(0, numSubjects);
      } else {
        return population;
      }
    } catch (Exception ex) {
      throw ex;
    }
  }
}
