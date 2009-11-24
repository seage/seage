
package org.seage.metaheuristic.sannealing;

/**
 *
 * @author Jan Zmátlík
 */
public interface ISimulatedAnnealingListener {

    public void simulatedAnnealingStarted( SimulatedAnnealingEvent e );

    public void simulatedAnnealingStopped( SimulatedAnnealingEvent e );

    public void newBestSolutionFound( SimulatedAnnealingEvent e );

    public void newCurrentSolutionFound( SimulatedAnnealingEvent e );

}
