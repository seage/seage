package org.seage.problem.sat.antcolony;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

public class SatGraphTest {
  @Test
  public void testSatGraph() throws Exception {
    IProblemProvider provider = ProblemProvider.getProblemProviders().get("SAT");
    ProblemInstanceInfo pii = provider.getProblemInfo().getProblemInstanceInfo("uf20-01");
    Formula formula = (Formula) provider.initProblemInstance(pii);

    SatGraph graph = new SatGraph(formula, new FormulaEvaluator(formula));
    assertNotNull(graph);
  }
}
