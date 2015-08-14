package org.seage.problem.sat.antcolony;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.sat.SatProblemProvider;

public class SatAntColonyFactoryTest extends AlgorithmFactoryTestBase
{

    public SatAntColonyFactoryTest()
    {
        super(new SatProblemProvider(), "AntColony");
    }

}
