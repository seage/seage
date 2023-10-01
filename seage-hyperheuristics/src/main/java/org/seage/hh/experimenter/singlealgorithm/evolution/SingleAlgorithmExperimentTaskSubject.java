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
  protected AlgorithmParams algorithmParams;
  protected String hash;

  /**
   * .
   *
   * @param newParamNames .
   * @param geneValues .
   */
  public SingleAlgorithmExperimentTaskSubject(String[] newParamNames, Double[] geneValues) {
    super(geneValues);
    paramNames = newParamNames; 
    this.algorithmParams = new AlgorithmParams(); // subject
    for (int i = 0; i < this.getChromosome().getLength(); i++) {
      this.algorithmParams.putValue(this.getParamNames()[i], this.getChromosome().getGene(i));
    }
  }

  public SingleAlgorithmExperimentTaskSubject(
      SingleAlgorithmExperimentTaskSubject experimentSubject) {
    super(experimentSubject);
    paramNames = experimentSubject.paramNames.clone();
    this.algorithmParams = new AlgorithmParams(); // subject
    for (int i = 0; i < this.getChromosome().getLength(); i++) {
      this.algorithmParams.putValue(this.getParamNames()[i], this.getChromosome().getGene(i));
    }
  }

  /**
   * .
   *
   * @return Returns hash of algorithm params;
   */
  public String getHash() {
    try {
      return this.algorithmParams.hash();
    } catch (Exception e) {
      logger.error("Error: Unable to set hashCode");
      return null;
    }
  }

  /**
   * Method returns algorithm params.
   *
   * @return algorithm params.
   */
  public AlgorithmParams getAlgorithmParams() {
    return this.algorithmParams;
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
