
package org.seage.tsp.metaheuristic.sannealing;

import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.tsp.data.*;

/**
 *
 * @author Jan Zmátlík
 */
public class TspMain implements ISimulatedAnnealingListener{


    public static void main(String[] args)
    {
        try
        {
            new TspMain().run("data/eil51.tsp");//args[0]
        }
        catch(Exception ex)
        {
            System.out.println( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        City[] cities = CityProvider.readCities(path);
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + cities.length);

        SimulatedAnnealing sa = new SimulatedAnnealing(new TspObjectiveFunction(), new TspMoveManager() );
        sa.setMaximalTemperature( 100 );
        sa.setMinimalTemperature( 1.778 );
        sa.setAnnealingCoefficient( 0.88 );
        sa.addSimulatedAnnealingListener( this );
        sa.startSearching(new TspSolution());

    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        System.out.println("Started");
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
        System.out.println("Stop");
    }

    public void newBestSolutionFound(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void newCurrentSolutionFound(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
