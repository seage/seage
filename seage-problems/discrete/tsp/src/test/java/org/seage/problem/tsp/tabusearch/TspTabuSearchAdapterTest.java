package org.seage.problem.tsp.tabusearch;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspTabuSearchAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {
    public TspTabuSearchAdapterTest() {
        super(new TspProblemProvider(), "TabuSearch");
    }
}
