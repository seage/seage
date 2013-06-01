package org.seage.experimenter.singlealgorithm.evolution;

import org.seage.metaheuristic.genetics.Subject;

public class ExperimentSubject extends Subject<Double> 
{

	public ExperimentSubject(Double[] geneValues) {
		super(geneValues);
		// TODO Auto-generated constructor stub
	}

	public ExperimentSubject(ExperimentSubject experimentSubject) {
		super(experimentSubject);
	}

	@Override
	public Subject<Double> clone() {
		ExperimentSubject result = new ExperimentSubject(this);
		return result;
	}
	
}
