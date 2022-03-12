package org.seage.aal.problem;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;

@Annotations.ProblemId("TEST")
@Annotations.ProblemName("Test Problem")
public class TestProblemProvider extends ProblemProvider<TestPhenotype> {

  public TestProblemProvider() {

  }

  @Override
  public ProblemInstance initProblemInstance(ProblemInstanceInfo problemInstanceInfo)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IPhenotypeEvaluator<TestPhenotype> initPhenotypeEvaluator(ProblemInstance problemInstance)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TestPhenotype[] generateInitialSolutions(ProblemInstance problemInstance, int numSolutions,
      long randomSeed) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo problemInstanceInfo)
      throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public ProblemMetadataGenerator<TestPhenotype> initProblemMetadataGenerator() {
    // TODO Auto-generated method stub
    return null;
  }
}
