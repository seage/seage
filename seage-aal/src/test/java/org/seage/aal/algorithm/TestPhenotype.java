package org.seage.aal.algorithm;

public class TestPhenotype extends Phenotype<Integer[]> {

  public TestPhenotype(Integer[] path) {
    super(path);
  }

  @Override
  public String toText() {
    // TODO Auto-generated method stub
    return solution.toString();
  }

  @Override
  public void fromText(String text) {
    // TODO Auto-generated method stub
  }

}
