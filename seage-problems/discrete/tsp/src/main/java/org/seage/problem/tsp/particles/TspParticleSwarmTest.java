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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Jan Zmatlik - Initial implementation
 */

package org.seage.problem.tsp.particles;

import java.io.FileInputStream;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is demonstration of PSO algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmTest implements IParticleSwarmListener {
  private static final Logger log = LoggerFactory.getLogger(TspParticleSwarmTest.class.getName());

  private City[] cities;
  // private static String _dataPath = "data/eil51.tsp";
  private static String dataPath = "data/berlin52.tsp";

  public static void main(String[] args) {
    try {
      if (args.length != 0) {
        dataPath = args[0];
      }
      new TspParticleSwarmTest().run(dataPath);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    cities = CityProvider.readCities(new FileInputStream(path));
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    ParticleSwarm pso = new ParticleSwarm(new TspObjectiveFunction(cities));

    pso.setMaximalIterationCount(10000);
    // pso.setMaximalVelocity(10);
    // pso.setMinimalVelocity(-10);
    // pso.setInertia( 0.9 );
    // pso.setAlpha(1.193);
    // pso.setBeta(0.721);

    pso.setMinimalVectorValue(-10);
    pso.setMaximalVectorValue(10);

    pso.setInertia(0.721);
    pso.setAlpha(0.5);
    pso.setBeta(0.2);

    pso.addParticleSwarmOptimizationListener(this);

    Particle[] particles = new Particle[10000];
    for (int i = 0; i < particles.length; i++) {
      particles[i] = new TspRandomParticle(cities.length);
    }
    pso.startSearching(particles);
  }

  @Override
  public void newBestSolutionFound(ParticleSwarmEvent e) {

    String s = String.format("Best (%d): %f", e.getParticleSwarm().getCurrentIteration(),
        e.getParticleSwarm().getBestParticle().getEvaluation());
    log.info(s);
  }

  @Override
  public void newIterationStarted(ParticleSwarmEvent e) {
    // Nothing here
  }

  @Override
  public void particleSwarmStarted(ParticleSwarmEvent e) {
    log.info("Started");
  }

  @Override
  public void particleSwarmStopped(ParticleSwarmEvent e) {
    log.info("Stopped");
  }

}
