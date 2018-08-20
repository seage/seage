package org.seage.problem.sat.antcolony;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

public class SatAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

    public SatAntColonyAdapterTest() {
        super(new SatProblemProvider(), "AntColony");
    }

}
