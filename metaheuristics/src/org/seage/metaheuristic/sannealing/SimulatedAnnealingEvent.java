
package org.seage.metaheuristic.sannealing;

/**
 *
 * @author Jan Zmátlík
 */
public class SimulatedAnnealingEvent extends java.util.EventObject {

    public SimulatedAnnealingEvent(Object source)
    {
        super( source );
    }

    public ISimulatedAnnealing getSimulatedAnnealing()
    {
        return (ISimulatedAnnealing) source;
    }

}
