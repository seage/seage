package org.seage.aal.algorithm.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestObjectiveFunction implements IObjectiveFunction
{

    @Override
    public void setObjectiveValue(Solution s)
    {
        s.setObjectiveValue(1);
    }

}
