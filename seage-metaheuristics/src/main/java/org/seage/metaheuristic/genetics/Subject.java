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
 */

package org.seage.metaheuristic.genetics;

/**
 * Genetic algorithm's subject implementation.
 * @author Richard Malek (original)
 */
public class Subject<GeneType> implements Cloneable {
  private Chromosome<GeneType> chromosome;
  private double[] fitness;
  private int hashCode;

  public Subject(GeneType[] geneValues) {
    chromosome = new Chromosome<>(geneValues);
    computeHash();
  }

  protected Subject(Subject<GeneType> subject) {
    chromosome = subject.getChromosome().clone(); // TODO: Replace by copy constructor
    fitness = subject.fitness;
    hashCode = subject.hashCode;
  }

  /*
   * Interface Solution
   */
  public double[] getObjectiveValue() {
    return fitness;
  }

  public void setObjectiveValue(double[] objValue) {
    fitness = objValue;
    computeHash();
  }

  @Override
  public Subject<GeneType> clone() {
    return new Subject<>(this);
  }

  /*
   * Subject's method
   */
  public Chromosome<GeneType> getChromosome() {
    return chromosome;
  }

  public double[] getFitness() {
    return getObjectiveValue();
  }

  public void computeHash() {
    int hash = 0x1000;
    for (int i = 0; i < chromosome.getLength(); i++) {
      hash = ((hash << 5) ^ (hash >> 27)) ^ (chromosome.getGene(i).hashCode() + 2) << 1;
    }
    hashCode = hash;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object subject0) {
    if (subject0 == null) {
      return false;
    }
    return subject0.hashCode() == hashCode;
  }

  @Override
  public String toString() {
    String result = "";

    if (fitness == null) {
      return "#" + hashCode;
    }

    for (int i = 0; i < chromosome.getLength(); i++) {
      result += (chromosome.getGene(i)) + " ";
    }

    return result;
  }
}
