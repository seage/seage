package org.seage.problem.tsp.sannealing;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspSimulatedAnnealingAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {
    public TspSimulatedAnnealingAdapterTest() {
        super(new TspProblemProvider(), "SimulatedAnnealing");
    }
}
