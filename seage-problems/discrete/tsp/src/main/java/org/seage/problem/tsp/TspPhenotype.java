package org.seage.problem.tsp;

import java.util.Arrays;

import org.seage.aal.algorithm.Phenotype;

public class TspPhenotype extends Phenotype<Integer[]> {

	public TspPhenotype(Integer[] path) {
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

//	@Override
//	public boolean isSame(Phenotype<Integer[]> p) {		
//		return Arrays.equals(_solution, p.getSolution());
//	}

}
