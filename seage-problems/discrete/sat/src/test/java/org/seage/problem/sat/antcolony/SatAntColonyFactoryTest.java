package org.seage.problem.sat.antcolony;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatAntColonyFactoryTest extends ProblemProviderTestBase
{

    public SatAntColonyFactoryTest()
    {
        super(new SatProblemProvider(), "AntColony");
    }

}
