package org.seage.problem.jssp.antcolony;

import org.seage.aal.algorithm.AlgorithmFactoryTestBase;
import org.seage.problem.jssp.JsspProblemProvider;

public class JsspAntColonyFactoryTest extends AlgorithmFactoryTestBase
{
    public JsspAntColonyFactoryTest()
    {
        super(new JsspProblemProvider(), "AntColony");
    }

}
