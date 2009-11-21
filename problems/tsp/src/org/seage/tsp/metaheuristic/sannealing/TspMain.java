
package org.seage.tsp.metaheuristic.sannealing;

import org.seage.metaheuristic.sannealing.OldTspClient;
import org.seage.tsp.data.*;

/**
 *
 * @author Jan Zmátlík
 */
public class TspMain {


    public static void main(String[] args)
    {
        try
        {
            TspMain.run("data/eil51.tsp");//args[0]
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void run(String path) throws Exception
    {
        City[] cities = CityProvider.readCities(path);
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + cities.length);

        System.out.println("Initiate TSPClient");
        OldTspClient tspClient = new OldTspClient(cities);
        
        System.out.println("Client running");
        tspClient.run();
    }
}
