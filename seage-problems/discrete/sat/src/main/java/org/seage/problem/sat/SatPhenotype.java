package org.seage.problem.sat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.seage.aal.algorithm.Phenotype;

public class SatPhenotype extends Phenotype<Boolean[]> {

  public SatPhenotype(Boolean[] path) {
    super(path);
  }

  @Override
  public String toText() {
    String res = Stream.of(solution).map(i -> i ? "1" : "0").collect(Collectors.joining(", "));
    return String.format("[%s]", res);
  }

  @Override
  public void fromText(String text) {
    String stringArray = text.substring(1, text.length() - 1);
    solution = Arrays.asList(stringArray.split(","))
        .stream().map(s -> Boolean.parseBoolean(s.strip()))
        .collect(Collectors.toList())
        .toArray(new Boolean[0]);
  }
}
