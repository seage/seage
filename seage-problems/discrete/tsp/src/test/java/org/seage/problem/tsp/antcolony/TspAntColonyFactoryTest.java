package org.seage.problem.tsp.antcolony;

import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.problem.tsp.TspProblemProvider;

public class TspAntColonyFactoryTest extends ProblemProviderTestBase
{

    public TspAntColonyFactoryTest()
    {
        super(new TspProblemProvider(), "AntColony");
    }

}
