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

package org.seage.aal.algorithm.particles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.Particle;

/**
 *
 * @author Richard Malek
 */
@Disabled("Adapter class not fully implemented yet")
public class ParticleSwarmAdapterTest extends AlgorithmAdapterTestBase<Particle> {

  public ParticleSwarmAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    algAdapter = new ParticleSwarmAdapter<TestPhenotype, Particle>(null, null, null, false) {

      @Override
      public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
        initialParticles = new Particle[source.length];

        for (int i = 0; i < source.length; i++) {
          initialParticles[i] = new Particle(source[i].getSolution().length);
          double[] coords = new double[source[i].getSolution().length];
          for (int j = 0; j < coords.length; j++) {
            coords[j] = source[i].getSolution()[j];
          }
          initialParticles[i].setCoords(coords);
        }

      }

      @Override
      public TestPhenotype[] solutionsToPhenotype() throws Exception {
        TestPhenotype[] result = new TestPhenotype[initialParticles.length];

        for (int i = 0; i < initialParticles.length; i++) {
          Integer[] coords = new Integer[initialParticles[i].getCoords().length];
          for (int j = 0; j < coords.length; j++) {
            coords[j] = (int) initialParticles[i].getCoords()[j];
          }
          result[i] = new TestPhenotype(coords);
        }
        return result;
      }

      @Override
      public TestPhenotype solutionToPhenotype(Particle solution) throws Exception {
        // TODO Auto-generated method stub
        return null;
      }
    };

    algParams = new AlgorithmParams();
    // _algParams.putValue("problemID", "ParticleSwarmTest");
    // _algParams.putValue("instance", "TestInstance");

    DataNode params = new DataNode("Parameters");
    params.putValue("maxIterationCount", 0);
    params.putValue("numSolutions", 0);
    params.putValue("maxVelocity", 0);
    params.putValue("minVelocity", 1);
    params.putValue("inertia", 0);
    params.putValue("alpha", 1);
    params.putValue("beta", 0);

    algParams.putDataNodeRef(params);
  }

  @Override
  @Test
  public void testAlgorithm() throws Exception {
    super.testAlgorithm();
  }

  @Override
  @Test
  public void testAlgorithmWithParamsAtZero() throws Exception {
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
    super.testAsyncRunning();
  }

  @Test
  @Override
  public void testReport() throws Exception {
    super.testReport();
  }

  @Test
  @Override
  public void testAlgorithmWithParamsNull() throws Exception {
    super.testAlgorithmWithParamsNull();
  }
}
