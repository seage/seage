
package org.seage.metaheuristic.sannealing;

/**
 *
 * @author Jan Zmátlík
 */
public interface Solution {

 public double getObjectiveValue();
 public abstract void setObjectiveValue( double objValue );

}
