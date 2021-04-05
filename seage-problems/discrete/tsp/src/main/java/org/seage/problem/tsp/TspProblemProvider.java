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
import org.seage.aal.problem.ProblemProvider;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("TSP")
@Annotations.ProblemName("Travelling Salesman Problem")
public class TspProblemProvider extends ProblemProvider<TspPhenotype> {

  @Override
  public TspProblemInstance initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    City[] cities;

    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getValue("path").toString();

    InputStream stream0;
    if (origin == ProblemInstanceOrigin.RESOURCE)
      stream0 = getClass().getResourceAsStream(path);
    else
      stream0 = new FileInputStream(path);

    try (InputStream stream = stream0) {
      cities = CityProvider.readCities(stream);
    } catch (Exception ex) {
      System.err.println("TspProblemProvider.initProblemInstance - readCities failed, path: " + path);
      throw ex;
    }

    return new TspProblemInstance(instanceInfo, cities);

  }

  @Override
  public TspPhenotype[] generateInitialSolutions(ProblemInstance instance, int numSolutions, long randomSeed)
      throws Exception {
    int numTours = numSolutions;
    City[] cities = ((TspProblemInstance) instance).getCities();
    TspPhenotype[] result = new TspPhenotype[numTours];

    // result[0] = new TspPhenotype(TourProvider.createGreedyTour(cities,
    // randomSeed));
    for (int i = 0; i < numTours; i++)
      result[i] = new TspPhenotype(TourProvider.createRandomTour(cities.length));

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
  public IPhenotypeEvaluator<TspPhenotype> initPhenotypeEvaluator(ProblemInstance instance) throws Exception {
    return new TspPhenotypeEvaluator(this.getProblemInfo(), (TspProblemInstance) instance);
  }

}
