
package org.seage.metaheuristic.sannealing;

/**
 *
 * @author Jan Zmátlík
 */
public class Solution implements java.lang.Cloneable {

    double value = Double.MAX_VALUE;

    public double getObjectiveValue()
    {
        return value;
    }

    public void setObjectiveValue(double objValue)
    {
        value = objValue;
    }

}
