package org.seage.problem.tsp;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.seage.aal.algorithm.Phenotype;

public class TspPhenotype extends Phenotype<Integer[]> {

  public TspPhenotype(Integer[] path) {
    super(path);
  }

  @Override
  public String toText() {
    return Arrays.asList(_solution).stream().map(i -> i.toString()).reduce((s, i) -> s + " " + i).get();
  }

  @Override
  public void fromText(String text) {
    _solution = Arrays.asList(text.split(" ")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList())
        .toArray(new Integer[0]);
  }
}
