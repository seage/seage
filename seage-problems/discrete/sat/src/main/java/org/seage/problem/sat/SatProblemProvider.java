/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation - Added problem
 * annotations
 */
package org.seage.problem.sat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

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
@Annotations.ProblemId("SAT")
@Annotations.ProblemName("Boolean Satisfiability Problem")
public class SatProblemProvider extends ProblemProvider<SatPhenotype> {
  @Override
  public Formula initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getPath();

    InputStream stream0;
    if (origin == ProblemInstanceOrigin.RESOURCE)
      stream0 = getClass().getResourceAsStream(path);
    else
      stream0 = new FileInputStream(path);

    Formula formula = null;

    try (InputStream stream = stream0) {
      formula = new Formula(instanceInfo, FormulaReader.readClauses(stream0));
    } catch (Exception ex) {
      System.err.println("SatProblemProvider.initProblemInstance - readCities failed, path: " + path);
      throw ex;
    }

    return formula;

  }

  @Override
  public IPhenotypeEvaluator<SatPhenotype> initPhenotypeEvaluator(ProblemInstance problemInstance) throws Exception {
    Formula f = (Formula) problemInstance;
    return new SatPhenotypeEvaluator(f);
  }

  @Override
  public SatPhenotype[] generateInitialSolutions(ProblemInstance problemInstance, int numSolutions, long randomSeed)
      throws Exception {
    Random rnd = new Random(randomSeed);
    Formula f = (Formula) problemInstance;
    SatPhenotype[] result = new SatPhenotype[numSolutions];

    for (int i = 0; i < numSolutions; i++) {
      Boolean[] array = new Boolean[f.getLiteralCount()];
      for (int j = 0; j < f.getLiteralCount(); j++)
        array[j] = rnd.nextBoolean();
      result[i] = new SatPhenotype(array);
    }
    return result;
  }

  @Override
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo problemInstanceInfo) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
