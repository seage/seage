/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.tsp.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Chromosome;
import org.seage.metaheuristic.genetics.Subject;

/**
 * TspGeneticOperator implementation.
 * 
 * @author Richard Malek
 */
public class TspGeneticOperator extends BasicGeneticOperator<Subject<Integer>, Integer> {
  /** . */
  public List<Subject<Integer>> crossOver2(Subject<Integer> parent1, Subject<Integer> parent2)
      throws Exception {
    int length = parent1.getChromosome().getLength();

    int[] occur1 = new int[length];
    int[] occur2 = new int[length];
    int[] cross1 = new int[length];
    int[] cross2 = new int[length];
    for (int i = 0; i < length; i++) {
      cross1[i] = cross2[i] = -1;
    }

    int ix = _random.nextInt(length);
    int crossLength = (int) (length * _random.nextDouble() * _crossLengthCoef) + 1;

    if (ix > length - crossLength) {
      ix = length - crossLength;
    }

    Subject<Integer> child1 = parent1.clone();
    Subject<Integer> child2 = parent2.clone();

    for (int i = ix; i < ix + crossLength; i++) {
      int x1 = child1.getChromosome().getGene(i);
      int x2 = child2.getChromosome().getGene(i);

      child1.getChromosome().setGene(i, x2);
      child2.getChromosome().setGene(i, x1);

      cross1[x2] = x1;
      cross2[x1] = x2;
    }
    Chromosome<Integer> ch1 = child1.getChromosome();
    Chromosome<Integer> ch2 = child2.getChromosome();

    for (int i = 0; i < length; i++) {
      if (i < ix || i >= ix + crossLength) {
        int c1 = 0;
        int c2 = 0;
        while (c1 != -1) {
          c1 = cross1[ch1.getGene(i)];
          if (c1 != -1) {
            ch1.setGene(i, c1);
          }
        }
        while (c2 != -1) {
          c2 = cross2[ch2.getGene(i)];
          if (c2 != -1) {
            ch2.setGene(i, c2);
          }
        }
      }
      occur1[ch1.getGene(i)]++;
      occur2[ch2.getGene(i)]++;
    }

    for (int i = 0; i < length; i++) {
      if (occur1[i] > 1 || occur2[i] > 1) {
        throw new RuntimeException("Invalid chromosome");
      }
    }

    ArrayList<Subject<Integer>> result = new ArrayList<Subject<Integer>>();
    result.add(child1);
    result.add(child2);
    return result;

  }

  @Override
  public List<Subject<Integer>> crossOver(Subject<Integer> parent1, Subject<Integer> parent2)
      throws Exception {
    int length = parent1.getChromosome().getLength();

    int ix = _random.nextInt(length);
    int crossLength = (int) (length * _random.nextDouble() * _crossLengthCoef) + 1;

    if (ix > length - crossLength) {
      ix = length - crossLength;
    }

    Subject<Integer> child1 = parent1.clone();
    Subject<Integer> child2 = parent2.clone();
    Chromosome<Integer> child1Chrom = child1.getChromosome();
    Chromosome<Integer> child2Chrom = child2.getChromosome();

    HashMap<Integer, Integer> chromVals1 = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> chromVals2 = new HashMap<Integer, Integer>();

    for (int i = 0; i < length; i++) {
      chromVals1.put(i + 1, child1Chrom.getGene(i));
      chromVals2.put(i + 1, child2Chrom.getGene(i));
    }

    for (int i = ix; i < ix + crossLength; i++) {

      int x1 = child1Chrom.getGene(i);
      int x2 = child2Chrom.getGene(i);

      if (x1 == x2)
        continue;

      // int y1 = findGeneValueIndex(x2, child1Chrom);
      // int y2 = findGeneValueIndex(x1, child2Chrom);
      int y1 = chromVals1.get(x2) - 1;
      int y2 = chromVals2.get(x1) - 1;

      child1Chrom.swapGenes(i, y1);
      child1Chrom.swapGenes(i, y2);
    }

    List<Subject<Integer>> result = new ArrayList<Subject<Integer>>();
    result.add(child1);
    result.add(child2);
    return result;

  }

}
