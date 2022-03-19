package org.seage.problem.qap;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.seage.aal.algorithm.Phenotype;

public class QapPhenotype extends Phenotype<Integer[]> {

  public QapPhenotype(Integer[] assignment) throws Exception {
    super(assignment);
  }

  @Override
  public String toText() {
    return Arrays.toString(solution);
  }

  @Override
  public void fromText(String text) {
    String stringArray = text.substring(1, text.length() - 1);
    solution = Arrays.asList(stringArray.split(","))
        .stream().map(s -> Integer.parseInt(s.strip()))
        .collect(Collectors.toList())
        .toArray(new Integer[0]);
  }
}
