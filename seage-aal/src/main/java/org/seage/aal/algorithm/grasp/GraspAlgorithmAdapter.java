package org.seage.aal.algorithm.grasp;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;

@AlgorithmParameters({ @Parameter(name = "numberOfRestarts", min = 0, max = 100, init = 10) })
public abstract class GraspAlgorithmAdapter extends AlgorithmAdapterImpl {
  public GraspAlgorithmAdapter() {
    super(null);
  }

  public void setParameters(AlgorithmParams params) throws Exception {
  }

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

  }

  @Override
  public void stopSearching() throws Exception {

  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    return null;
  }

}
