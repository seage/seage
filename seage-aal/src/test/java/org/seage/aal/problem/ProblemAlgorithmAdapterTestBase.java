package org.seage.aal.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.data.ObjectCloner;

public abstract class ProblemAlgorithmAdapterTestBase<P extends Phenotype<?>> {
  protected IAlgorithmFactory<P, ?> algorithmFactory;
  protected IProblemProvider<P> problemProvider;
  protected String algorithmID;

  /** . */
  public ProblemAlgorithmAdapterTestBase(IProblemProvider<P> problemProvider, String algorithmID) {
    this.problemProvider = problemProvider;
    this.algorithmID = algorithmID;
    assertNotNull(problemProvider);

    try {
      assertNotNull(problemProvider.getProblemInfo());
      algorithmFactory = problemProvider.getAlgorithmFactory(algorithmID);
      assertNotNull(algorithmFactory);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  void testGetAlgorithmClass() {
    assertNotNull(algorithmFactory.getAlgorithmClass());
  }

  @Test
  void testAlgorithmAdapter() throws Exception {
    List<ProblemInstanceInfo> infos = problemProvider.getProblemInfo().getProblemInstanceInfos();
    assertTrue(infos.size() > 0);

    ProblemInstanceInfo pii = findSmallestInstance(infos);
    ProblemInstance pin = problemProvider.initProblemInstance(pii);
    IPhenotypeEvaluator<P> phenotypeEvaluator = problemProvider.initPhenotypeEvaluator(pin);
    assertNotNull(phenotypeEvaluator);

    IAlgorithmAdapter<P, ?> aa = algorithmFactory
        .createAlgorithm(problemProvider.initProblemInstance(pii), phenotypeEvaluator);
    assertNotNull(aa);

    P[] solutions =
        problemProvider.generateInitialSolutions(problemProvider.initProblemInstance(pii), 10, 1);
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

      assertNotNull(solutions[i].getObjValue());
      assertNotNull(solutions[i].getScore());

      byte[] b1 = ObjectCloner.getBytes(solutions[i].getSolution());
      byte[] b2 = ObjectCloner.getBytes(solutions2[i].getSolution());
      assertTrue(Arrays.equals(b1, b2),
          "Arrays not equal: " + b1.toString() + " vs. " + b2.toString());
    }

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());
    solutions = problemProvider.generateInitialSolutions(problemProvider.initProblemInstance(pii),
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
    // assertNotEquals(report.getDataNode("Log").getDataNodes("NewSolution").size(), 0);
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID);
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 1);
    result.putValue("numSolutions", 1);
    return result;
  }

  private ProblemInstanceInfo findSmallestInstance(List<ProblemInstanceInfo> infos) {
    return infos.stream().sorted((p1, p2) -> {
      try {
        return p1.getValueInt("size") - p2.getValueInt("size");
      } catch (Exception e) {
        return 0;
      }
    }).findFirst().get();
  }
}
