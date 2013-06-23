package org.seage.experimenter.singlealgorithm.evolution;

import org.seage.metaheuristic.genetics.Subject;

public class SingleAlgorithmExperimentTaskSubject extends Subject<Double> 
{

	public SingleAlgorithmExperimentTaskSubject(Double[] geneValues) {
		super(geneValues);
		// TODO Auto-generated constructor stub
	}

	public SingleAlgorithmExperimentTaskSubject(SingleAlgorithmExperimentTaskSubject experimentSubject) {
		super(experimentSubject);
	}

	@Override
	public Subject<Double> clone() {
		SingleAlgorithmExperimentTaskSubject result = new SingleAlgorithmExperimentTaskSubject(this);
		return result;
	}
	
}
