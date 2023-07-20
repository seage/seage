package org.seage.hh.experimenter.singlealgorithm.evolution;

import org.seage.metaheuristic.genetics.Subject;

/**
 * .
 */
public class SingleAlgorithmExperimentTaskSubject extends Subject<Double> {
  protected String[] paramNames;

  public SingleAlgorithmExperimentTaskSubject(String[] newParamNames, Double[] geneValues) {
    super(geneValues);
    paramNames = newParamNames; 
  }

  public SingleAlgorithmExperimentTaskSubject(
      SingleAlgorithmExperimentTaskSubject experimentSubject) {
    super(experimentSubject);
    paramNames = experimentSubject.paramNames.clone();
  }

  @Override
  public Subject<Double> clone() {
    SingleAlgorithmExperimentTaskSubject result = new SingleAlgorithmExperimentTaskSubject(this);
    return result;
  }

  public String[] getParamNames() {
    return paramNames;
  }

}
