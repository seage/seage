package org.seage.problem.sat.tabusearch;

import org.seage.metaheuristic.tabusearch.SolutionAdapter;

/**
 * Summary description for SatSolution.
 */
public class SatSolution extends SolutionAdapter {
  private boolean[] _literalValues;
  private int _hash;

  public SatSolution(boolean[] literalValues) {
    _literalValues = literalValues.clone();
    // _hash = SatPhenotypeEvaluator.hashCode(_literalValues);
  }

  public boolean[] getLiteralValues() {
    return _literalValues;
  }

  @Override
  public Object clone() {
    SatSolution copy = (SatSolution) super.clone();

    copy._literalValues = new boolean[_literalValues.length];
    for (int i = 0; i < _literalValues.length; i++) {
      copy._literalValues[i] = _literalValues[i];
    }

    return copy;
  }

  @Override
  public String toString() {
    String result = super.toString();

    String str = "";
    for (int i = 0; i < _literalValues.length; i++) {
      int val = _literalValues[i] == true ? 1 : 0;
      str += val;
    }
    return result + "\t" + str;
  }

  @Override
  public int hashCode() {
    return _hash;
  }

  @Override
  public boolean equals(Object satSolution) {
    return satSolution != null && this.hashCode() == satSolution.hashCode();
  }
}
