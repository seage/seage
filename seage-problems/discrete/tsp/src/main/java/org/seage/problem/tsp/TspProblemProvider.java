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
 *     - Added problem annotations
 */

package org.seage.problem.tsp;

import java.io.FileInputStream;
import java.io.InputStream;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.aal.problem.ProblemProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TspProblemProvider.
 * @author Richard Malek
 */
@Annotations.ProblemId("TSP")
@Annotations.ProblemName("Travelling Salesman Problem")
public class TspProblemProvider extends ProblemProvider<TspPhenotype> {
  private static Logger logger = LoggerFactory.getLogger(TspProblemProvider.class.getName());

  @Override
  public TspProblemInstance initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    City[] cities;

    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getValue("path").toString();

    InputStream stream0;
    if (origin == ProblemInstanceOrigin.RESOURCE) {
      stream0 = getClass().getResourceAsStream(path);
    } else {
      stream0 = new FileInputStream(path);
    }

    try (InputStream stream = stream0) {
      cities = CityProvider.readCities(stream);
    } catch (Exception ex) {
      logger.error("TSP ReadCities failed, path: {}", path);
      throw ex;
    }

    return new TspProblemInstance(instanceInfo, cities);

  }

  @Override
  public TspPhenotype[] generateInitialSolutions(
      ProblemInstance instance, int numSolutions, long randomSeed)
      throws Exception {
    int numTours = numSolutions;
    City[] cities = ((TspProblemInstance) instance).getCities();
    TspPhenotype[] result = new TspPhenotype[numTours];
    IPhenotypeEvaluator<TspPhenotype> evaluator = this.initPhenotypeEvaluator(instance);

    for (int i = 0; i < numTours; i++) {
      if (i == 0) {
        result[i] = new TspPhenotype(TourProvider.createGreedyTour(
            cities, System.currentTimeMillis()));
      } else {
        result[i] = new TspPhenotype(TourProvider.createRandomTour(cities.length));
      }
      double[] objVals = evaluator.evaluate(result[i]);
      result[i].setObjValue(objVals[0]);
      result[i].setScore(objVals[1]);
    }

    return result;
  }

  @Override
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo instance) throws Exception {
    // Integer[] tour = (Integer[])solution;

    // TODO: A - Implement visualize method
    // String outPath =
    // _problemParams.getDataNode("visualizer").getValueStr("outPath");
    // int width = _problemParams.getDataNode("visualizer").getValueInt("width");
    // int height = _problemParams.getDataNode("visualizer").getValueInt("height");
    //
    // Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
  }

  @Override
  public IPhenotypeEvaluator<TspPhenotype> initPhenotypeEvaluator(ProblemInstance instance) 
      throws Exception {
    return new TspPhenotypeEvaluator(this.getProblemInfo(), (TspProblemInstance) instance);
  }

  @Override
  public ProblemMetadataGenerator<TspPhenotype> initProblemMetadataGenerator() {
    return new TspProblemMetadataGenerator(this);
  }

}
