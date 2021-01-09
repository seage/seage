package org.seage.aal.algorithm;

import org.seage.aal.algorithm.Phenotype;

public class TestPhenotype extends Phenotype<Integer[]> {

  public TestPhenotype(Integer[] path) {
    super(path);
  }

  @Override
  public String toText() {
    // TODO Auto-generated method stub
    return _solution.toString();
  }

  @Override
  public void fromText(String text) {
    // TODO Auto-generated method stub

  }

}
