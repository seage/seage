package org.seage.aal.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.data.ObjectCloner;

public abstract class ProblemAlgorithmAdapterTestBase<P extends Phenotype<?>> {
  protected IAlgorithmFactory<P, ?> _algorithmFactory;
  protected IProblemProvider<P> _problemProvider;
  protected String _algorithmID;

  public ProblemAlgorithmAdapterTestBase(IProblemProvider<P> problemProvider, String algorithmID) {
    _problemProvider = problemProvider;
    _algorithmID = algorithmID;
    assertNotNull(_problemProvider);

    try {
      assertNotNull(_problemProvider.getProblemInfo());
      _algorithmFactory = _problemProvider.getAlgorithmFactory(_algorithmID);
      assertNotNull(_algorithmFactory);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testGetAlgorithmClass() {
    assertNotNull(_algorithmFactory.getAlgorithmClass());
  }

  @Test
  public void testAlgorithmAdapter() throws Exception {
    List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
    assertTrue(infos.size() > 0);
    ProblemInstanceInfo pii = infos.get(0);
    ProblemInstance pin = _problemProvider.initProblemInstance(pii);
    IPhenotypeEvaluator<P> phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(pin);
    assertNotNull(phenotypeEvaluator);

    IAlgorithmAdapter<P, ?> aa = _algorithmFactory.createAlgorithm(_problemProvider.initProblemInstance(pii),
        phenotypeEvaluator);
    assertNotNull(aa);

    P[] solutions = _problemProvider.generateInitialSolutions(_problemProvider.initProblemInstance(pii), 10, 1);
    assertNotNull(solutions);
    aa.solutionsFromPhenotype(solutions);
    P[] solutions2 = aa.solutionsToPhenotype();
    assertNotNull(solutions2);
    assertEquals(solutions.length, solutions2.length);

    for (int i = 0; i < solutions.length; i++) {
      assertNotNull(solutions[i]);
      assertNotNull(solutions[i].getSolution());
      assertNotNull(solutions2[i]);
      assertNotNull(solutions2[i].getSolution());

      byte[] b1 = ObjectCloner.getBytes(solutions[i].getSolution());
      byte[] b2 = ObjectCloner.getBytes(solutions2[i].getSolution());
      assertTrue(Arrays.equals(b1, b2));
    }

    AlgorithmParams params = createAlgorithmParams(_problemProvider.getProblemInfo());
    solutions = _problemProvider.generateInitialSolutions(_problemProvider.initProblemInstance(pii),
        params.getValueInt("numSolutions"), 1);
    aa.solutionsFromPhenotype(solutions);
    aa.startSearching(params);

    solutions2 = aa.solutionsToPhenotype();
    assertNotNull(solutions2);
    assertEquals(solutions.length, solutions2.length);

    for (int i = 0; i < solutions.length; i++) {
      assertNotNull(solutions[i]);
      assertNotNull(solutions2[i]);
      // boolean theSame=true;
      // for(int j=0;j<solutions[i].length;j++)
      // {
      // if(!solutions[i][j].equals( solutions2[i][j]))
      // {
      // theSame=false;
      // break;
      // }
      // }
      // assertFalse(theSame);
    }
    AlgorithmReport report = aa.getReport();
    assertNotNull(report);
    assertNotSame(report.getDataNode("Log").getDataNodes("NewSolution").size(), 0);
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById(_algorithmID);
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 1);
    result.putValue("numSolutions", 1);
    return result;
  }
}
