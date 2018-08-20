package org.seage.problem.sat;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.seage.aal.algorithm.Phenotype;

public class SatPhenotype extends Phenotype<Boolean[]> {

	public SatPhenotype(Boolean[] path) {
		super(path);
	}

	@Override
	public String toText() {
		return Arrays.asList(_solution)
				.stream()
				.map(i -> i?"1":"0")
				.reduce((s, i) -> s +" "+i)
				.get();
	}

	@Override
	public void fromText(String text) {		
		_solution = Arrays.asList(text.split(" "))
				.stream()
				.map(s -> Boolean.parseBoolean(s))
				.collect(Collectors.toList())
				.toArray(new Boolean[0]);
	}
}
