package org.seage.aal.algorithm;

import org.seage.data.HashHelper;

public abstract class Phenotype<T> {
  protected String instanceID;
  protected T solution;
  protected Double objValue;
  /** Score - e.g. given by the UmitMetric. */
  protected Double score;
  protected String hash;

  /**
   * Generic Phenotype.
   * @param solution Problem specific solution.
   * @throws Exception Thrown when computing hash fails.
   */
  public Phenotype(T solution) throws Exception {
    this.solution = solution;
    this.hash = computeHash();
    this.objValue = null;
    this.score = null;
  }

  public T getSolution() {
    return this.solution;
  }

  public abstract String toText();

  public abstract void fromText(String text);

  public String computeHash() throws Exception {
    return HashHelper.hashFromString(toText());    
  }

  public Double getObjValue() {
    return objValue;
  }

  public void setObjValue(Double objValue) {
    this.objValue = objValue;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }
}
