package org.seage.problem.tsp.grasp;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.problem.tsp.TspPhenotype;

public class TspGraspAlgorithmAdapter implements IAlgorithmAdapter<TspPhenotype, TspSolution> {

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void startSearching(AlgorithmParams params, boolean async) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void stopSearching() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isRunning() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public TspPhenotype[] solutionsToPhenotype() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TspPhenotype solutionToPhenotype(TspSolution solution) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
