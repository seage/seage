package org.seage.problem.sat.grasp;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.grasp.GraspAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;

@Annotations.AlgorithmId("GRASP")
@Annotations.AlgorithmName("GRASP")
@Annotations.Broken("Still some troubles")
public class SatGraspAlgorithmFactory implements IAlgorithmFactory {

  @Override
  public Class<?> getAlgorithmClass() {
    return GraspAlgorithmAdapter.class;
  }

  @Override
  public IAlgorithmAdapter createAlgorithm(ProblemInstance instance, IPhenotypeEvaluator phenotypeEvaluator)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
