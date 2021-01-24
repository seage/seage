package org.seage.problem.tsp;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.seage.aal.algorithm.Phenotype;

public class TspPhenotype extends Phenotype<Integer[]> {

  public TspPhenotype(Integer[] path) {
    super(path);
  }

  @Override
  public String toText() {
    String res = Stream.of(solution).map(i -> i.toString()).collect(Collectors.joining(", "));
    return String.format("[%s]", res);
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
