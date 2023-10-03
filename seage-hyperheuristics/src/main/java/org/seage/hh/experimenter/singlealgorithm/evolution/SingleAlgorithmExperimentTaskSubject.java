package org.seage.hh.experimenter.singlealgorithm.evolution;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.metaheuristic.genetics.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 */
public class SingleAlgorithmExperimentTaskSubject extends Subject<Double> {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmExperimentTaskSubject.class.getName());
  protected String[] paramNames;

  /**
   * .
   *
   * @param newParamNames .
   * @param geneValues .
   */
  public SingleAlgorithmExperimentTaskSubject(String[] newParamNames, Double[] geneValues) {
    super(geneValues);
    paramNames = newParamNames;
  }

  public SingleAlgorithmExperimentTaskSubject(
      SingleAlgorithmExperimentTaskSubject experimentSubject) {
    super(experimentSubject);
    paramNames = experimentSubject.paramNames.clone();
  }

  /**
   * Method returns algorithm params.
   *
   * @return algorithm params.
   */
  public AlgorithmParams getAlgorithmParams() {
    AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
    for (int i = 0; i < this.getChromosome().getLength(); i++) {
      algorithmParams.putValue(this.getParamNames()[i], this.getChromosome().getGene(i));
    }
    return algorithmParams;
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
