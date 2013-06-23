package org.seage.experimenter.singlealgorithm.evolution;

import org.seage.metaheuristic.genetics.Subject;

public class ExperimentTaskSubject extends Subject<Double> 
{

	public ExperimentTaskSubject(Double[] geneValues) {
		super(geneValues);
		// TODO Auto-generated constructor stub
	}

	public ExperimentTaskSubject(ExperimentTaskSubject experimentSubject) {
		super(experimentSubject);
	}

	@Override
	public Subject<Double> clone() {
		ExperimentTaskSubject result = new ExperimentTaskSubject(this);
		return result;
	}
	
}
