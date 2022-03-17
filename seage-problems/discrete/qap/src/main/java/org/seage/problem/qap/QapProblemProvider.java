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
 * Contributors: Karel Durkota - Initial implementation Richard Malek - Added problem annotations
 */

package org.seage.problem.qap;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.aal.problem.ProblemProvider;

/**
 *
 * @author Karel Durkota
 */
@Annotations.ProblemId("QAP")
@Annotations.ProblemName("Quadratic Assignment Problem")
public class QapProblemProvider extends ProblemProvider<QapPhenotype> {

  @Override
  public ProblemInstance initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getPath();

    InputStream stream;
    if (origin == ProblemInstanceOrigin.RESOURCE)
      stream = getClass().getResourceAsStream(path);
    else
      stream = new FileInputStream(path);

    // params.getDataNode("evaluator").putValue("cities", _cities);
    return new QapProblemInstance(instanceInfo,
        FacilityLocationProvider.readFacilityLocations(stream));

  }

  @Override
  public QapPhenotype[] generateInitialSolutions(ProblemInstance instance, int numSolutions,
      long randomSeed) throws Exception {
    int numAssigns = numSolutions;
    Double[][][] facilityLocation = ((QapProblemInstance) instance).getFacilityLocation();
    int assignPrice = facilityLocation[0].length;
    QapPhenotype[] result = new QapPhenotype[numAssigns];
    // Integer[][] result = new Integer[numAssigns][assignPrice];

    // Random r = new Random();
    ArrayList<Integer> al = new ArrayList<Integer>();
    for (int i = 0; i < assignPrice; i++)
      al.add(i);
    for (int i = 0; i < numAssigns; i++) {
      Collections.shuffle(al);
      result[i] = new QapPhenotype(al.toArray(new Integer[] {}));
    }
    return result;
  }

  @Override
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo instance) throws Exception {
    // TODO: A - Implement visualize method
    // String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
    // int width = _problemParams.getDataNode("visualizer").getValueInt("width");
    // int height = _problemParams.getDataNode("visualizer").getValueInt("height");
    //
    // Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
  }

  @Override
  public IPhenotypeEvaluator<QapPhenotype> initPhenotypeEvaluator(ProblemInstance instance)
      throws Exception {
    return new QapPhenotypeEvaluator((QapProblemInstance) instance);
  }

  @Override
  public ProblemMetadataGenerator<QapPhenotype> initProblemMetadataGenerator() {
    return new QapProblemMetadataGenerator(this);
  }
}
