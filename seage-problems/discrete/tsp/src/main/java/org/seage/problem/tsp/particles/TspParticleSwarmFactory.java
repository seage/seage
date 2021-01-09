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
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.tsp.particles;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.particles.ParticleSwarmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Jan Zmatlik
 */
@Annotations.AlgorithmId("ParticleSwarm")
@Annotations.AlgorithmName("Particle Swarm")
public class TspParticleSwarmFactory implements IAlgorithmFactory<TspPhenotype, TspParticle> {
  private int _numParticles;

  private TspObjectiveFunction _objectiveFunction;

  @Override
  public Class<?> getAlgorithmClass() {
    return ParticleSwarmAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<TspPhenotype, TspParticle> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception {
    final City[] cities = ((TspProblemInstance) instance).getCities();

    _objectiveFunction = new TspObjectiveFunction(cities);

    IAlgorithmAdapter<TspPhenotype, TspParticle> algorithm = new ParticleSwarmAdapter<TspPhenotype, TspParticle>(
        generateInitialSolutions(cities), _objectiveFunction, phenotypeEvaluator, false) {
      @Override
      public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception {
        _numParticles = source.length;
        _initialParticles = new TspParticle[source.length];
        for (int i = 0; i < source.length; i++) {
          Integer[] tour = ((TspParticle) _initialParticles[i]).getTour();
          _initialParticles[i] = new TspSortedParticle(cities.length);
          for (int j = 0; j < source[i].getSolution().length; j++) {
            // _initialParticles[i].setCoords(coords); ???
          }
        }
      }

      @Override
      public TspPhenotype[] solutionsToPhenotype() throws Exception {
        // int numOfParticles = _particleSwarm.getParticles().length;
        TspPhenotype[] source = new TspPhenotype[_initialParticles.length];

        for (int i = 0; i < source.length; i++) {
          Integer[] tour = ((TspParticle) _initialParticles[i]).getTour();
          source[i] = new TspPhenotype(tour);
        }

        // TODO: A - need to sort source by fitness function of each tour

        return source;
      }

      @Override
      public TspPhenotype solutionToPhenotype(TspParticle solution) throws Exception {
        // TODO Auto-generated method stub
        return null;
      }
    };

    return algorithm;
  }

  private Particle[] generateInitialSolutions(City[] cities) throws Exception {
    Particle[] particles = generateTspRandomParticles(_numParticles, cities);

    for (Particle particle : particles) {
      // Initial coords
      for (int i = 0; i < cities.length; i++)
        particle.getCoords()[i] = Math.random();

      // Initial velocity
      for (int i = 0; i < cities.length; i++)
        particle.getVelocity()[i] = Math.random();

      // Evaluate
      _objectiveFunction.setObjectiveValue(particle);
    }

    return particles;
  }

  void printArray(Integer[] array) {
    for (int i = 0; i < array.length; i++)
      System.out.print(" " + array[i]);
  }

  private Particle[] generateTspRandomParticles(int count, City[] cities) {
    TspRandomParticle[] particles = new TspRandomParticle[count];
    for (int i = 0; i < count; i++)
      particles[i] = new TspRandomParticle(cities.length);

    return particles;
  }
}
