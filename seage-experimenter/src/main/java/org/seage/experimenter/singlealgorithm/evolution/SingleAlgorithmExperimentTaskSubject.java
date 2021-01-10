package org.seage.experimenter.singlealgorithm.evolution;

import org.seage.metaheuristic.genetics.Subject;

public class SingleAlgorithmExperimentTaskSubject extends Subject<Double> {
  protected String[] _paramNames;

  public SingleAlgorithmExperimentTaskSubject(String[] paramNames, Double[] geneValues) {
    super(geneValues);
    _paramNames = paramNames;
  }

  public SingleAlgorithmExperimentTaskSubject(SingleAlgorithmExperimentTaskSubject experimentSubject) {
    super(experimentSubject);
    _paramNames = experimentSubject._paramNames.clone();
  }

  @Override
  public Subject<Double> clone() {
    SingleAlgorithmExperimentTaskSubject result = new SingleAlgorithmExperimentTaskSubject(this);
    return result;
  }

  public String[] getParamNames() {
    return _paramNames;
  }

}
