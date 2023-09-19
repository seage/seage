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
 * .
 * Contributors:
 *   Jan Zmatlik
 *   - Initial implementation
 */

package org.seage.problem.rosenbrock.particles;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;

/**
 * .
 *
 * @author Jan Zmatlik
 */
public class RosenbrockParticleSwarmTest implements IParticleSwarmListener {
  @Test
  void testRosenbrockParticleSwarm() throws Exception {
    new RosenbrockParticleSwarmTest().start();
    assertNull(null);
  }
      
  /**
   * Rosenbrock Particle Swarm test.
   */
  public void start() {
    int dimension = 30;
    int numberOfParticles = 1000;
    //int maximalVelocity = 1.0d;

    RosenbrockObjectiveFunction objFunction = new RosenbrockObjectiveFunction();

    Particle[] particles = generateParticles(numberOfParticles, dimension);

    for (Particle particle : particles) {
      // Initial coords
      for (int i = 0; i < dimension; i++) {
        particle.getCoords()[i] = Math.random();
      }
      // Initial velocity
      for (int i = 0; i < dimension; i++) { 
        particle.getVelocity()[i] = Math.random();
      }
      // Evaluate
      objFunction.setObjectiveValue(particle);
    }

    ParticleSwarm pso = new ParticleSwarm(objFunction);
    pso.addParticleSwarmOptimizationListener(this);
    pso.setMaximalIterationCount(10000);
    // pso.setMaximalVelocity( 10 );
    // pso.setMinimalVelocity( -10 );
    // pso.setInertia( 0.92 );
    // pso.setAlpha(0.2);
    // pso.setBeta(0.5);
    
    pso.setMaximalVectorValue(10);
    pso.setMinimalVectorValue(-10);
    pso.setInertia(0.721);
    pso.setAlpha(1.193);
    pso.setBeta(1.193);
    
    pso.startSearching(particles);
    
    for (int i = 0; i < pso.getBestParticle().getCoords().length; i++) {
      System.out.print(pso.getBestParticle().getCoords()[i] + " ");
    }
    System.out.println();
  }

  private static Particle[] generateParticles(int count, int dimension) {
    Particle[] particles = new Particle[count];
    for (int i = 0; i < count; i++) {
      particles[i] = new Particle(dimension);
    }

    return particles;
  }
      
  public void newBestSolutionFound(ParticleSwarmEvent e) {
    System.out.println("Best: " + e.getParticleSwarm().getBestParticle().getEvaluation());
  }

  public void newIterationStarted(ParticleSwarmEvent e) {
    // Empty method    
  }

  public void particleSwarmStarted(ParticleSwarmEvent e) {
    System.out.println("Started");
  }

  public void particleSwarmStopped(ParticleSwarmEvent e) {
    System.out.println("Stopped");
  }
}
