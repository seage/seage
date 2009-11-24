
package org.seage.tsp.metaheuristic.sannealing;

import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmátlík
 */
public class TspSolution implements Solution {

    double value = 0.0;

    public double getObjectiveValue()
    {
        return value;
    }

    public void setObjectiveValue(double objValue)
    {
        value = objValue;
    }


}
