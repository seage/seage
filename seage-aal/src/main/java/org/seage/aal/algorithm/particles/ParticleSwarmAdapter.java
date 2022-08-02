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
 *     Richard Malek
 *     - Added annotations
 */

package org.seage.aal.algorithm.particles;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;

/**
 * ParticleSwarmAdapter base implementation.
 * @author Jan Zmatlik
 */
@AlgorithmParameters({ @Parameter(name = "maxIterationCount", min = 0, max = 1000000, init = 1000),
    @Parameter(name = "numSolutions", min = 1, max = 1000000, init = 100),
    @Parameter(name = "maxVelocity", min = 0, max = 10000, init = 1),
    @Parameter(name = "minVelocity", min = 0, max = 10000, init = 1),
    @Parameter(name = "inertia", min = 0, max = 10000, init = 10),
    @Parameter(name = "alpha", min = 0, max = 100, init = 1), 
    @Parameter(name = "beta", min = 0, max = 100, init = 1) })
public abstract class ParticleSwarmAdapter<P extends Phenotype<?>, S extends Particle>
    extends AlgorithmAdapterImpl<P, S> implements IParticleSwarmListener {
  protected ParticleSwarm particleSwarm;
  protected Particle[] initialParticles;
  private Particle bestParticle;
  private AlgorithmParams algParams;

  private long numberOfIterations = 0;
  private long numberOfNewSolutions = 0;
  private long lastIterationNumberNewSolution = 0;
  private double initObjectiveValue = Double.MAX_VALUE;
  private double evaluationsOfParticles = 0;
  private double avgOfBestSolutions = 0;

  protected ParticleSwarmAdapter(
      Particle[] initialParticles, IObjectiveFunction objectiveFunction,
      IPhenotypeEvaluator<P> phenotypeEvaluator, boolean maximizing) throws Exception {
    super(phenotypeEvaluator);
    this.initialParticles = initialParticles;
    this.particleSwarm = new ParticleSwarm(objectiveFunction);
  }

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

    reporter = new AlgorithmReporter<>(phenotypeEvaluator);
    reporter.putParameters(algParams);
    particleSwarm.startSearching(initialParticles);
  }

  @Override
  public void stopSearching() throws Exception {
    particleSwarm.stopSearching();

    while (isRunning()) {
      Thread.sleep(100);
    }
  }

  @Override
  public boolean isRunning() {
    return particleSwarm.isRunning();
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    reporter.putStatistics(
        numberOfIterations, numberOfNewSolutions, lastIterationNumberNewSolution,
        initObjectiveValue, avgOfBestSolutions, bestParticle.getEvaluation());

    return reporter.getReport();
  }

  /**
   * Sets the new algorithm parameters.
   * @param params .
   * @throws Exception .
   */
  public void setParameters(AlgorithmParams params) throws Exception {
    algParams = params;
    algParams.putValue("id", "ParticleSwarm");
    DataNode p = params.getDataNode("Parameters");
    particleSwarm.setMaximalIterationCount(p.getValueLong("maxIterationCount"));
    particleSwarm.setMaximalVectorValue(p.getValueDouble("maxVelocity"));
    particleSwarm.setMinimalVectorValue(p.getValueDouble("minVelocity"));
    particleSwarm.setInertia(p.getValueDouble("inertia"));
    particleSwarm.setAlpha(p.getValueDouble("alpha"));
    particleSwarm.setBeta(p.getValueDouble("beta"));

    particleSwarm.addParticleSwarmOptimizationListener(this);
  }

  // ############################ EVENTS ###############################//
  @Override
  public void newBestSolutionFound(ParticleSwarmEvent e) {
    bestParticle = e.getParticleSwarm().getBestParticle();
    numberOfNewSolutions++;
    evaluationsOfParticles += bestParticle.getEvaluation();
    avgOfBestSolutions = evaluationsOfParticles / numberOfNewSolutions;
    lastIterationNumberNewSolution = numberOfIterations;
  }

  @Override
  public void newIterationStarted(ParticleSwarmEvent e) {
    numberOfIterations++;
  }

  @Override
  public void particleSwarmStarted(ParticleSwarmEvent e) {
    initObjectiveValue = e.getParticleSwarm().getBestParticle().getEvaluation();
  }

  @Override
  public void particleSwarmStopped(ParticleSwarmEvent e) {
    bestParticle = e.getParticleSwarm().getBestParticle();
  }

}
