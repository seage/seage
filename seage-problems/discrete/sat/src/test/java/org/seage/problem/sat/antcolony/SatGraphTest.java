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

package org.seage.problem.sat.antcolony;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.SatProblemProvider;

/**
 * .
 */
public class SatGraphTest {
  @Test
  public void testSatGraph() throws Exception {
    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });

    IProblemProvider provider = ProblemProvider.getProblemProviders().get("SAT");
    ProblemInstanceInfo pii = provider.getProblemInfo().getProblemInstanceInfo("uf20-01");
    Formula formula = (Formula) provider.initProblemInstance(pii);

    SatGraph graph = new SatGraph(formula.getLiteralCount());
    assertNotNull(graph);
  }
}
