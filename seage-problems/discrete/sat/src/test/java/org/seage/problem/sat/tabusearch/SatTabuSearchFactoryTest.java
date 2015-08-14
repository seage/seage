package org.seage.problem.sat.tabusearch;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatTabuSearchFactoryTest extends AlgorithmFactoryTestBase
{

    public SatTabuSearchFactoryTest()
    {
        super(new SatProblemProvider(), "TabuSearch");
    }

}
