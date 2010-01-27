/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp;

import java.util.Random;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.data.DataNode;
import org.seage.problem.tsp.genetics.TspGeneticAlgorithmFactory;
import org.seage.problem.tsp.particles.TspParticleSwarmFactory;
import org.seage.problem.tsp.sannealing.TspSimulatedAnnealingFactory;
import org.seage.problem.tsp.tabusearch.TspTabuSearchFactory;

/**
 *
 * @author rick
 */
public class TspProblemProvider implements IProblemProvider
{
    private static City[] _cities;
    private int currentInstanceIx = -1;

    public void initProblemInstance(DataNode params, int instanceIx) throws Exception
    {
        if(currentInstanceIx != instanceIx)
        {
            currentInstanceIx = instanceIx;
            DataNode info = params.getDataNode("instance", instanceIx);
            String path = info.getValueStr("path");

            if(path.equals("[circle]"))
                _cities = CityProvider.generateCircleCities(info.getValueInt("numberOfCities"));
            else
                _cities = CityProvider.readCities(info.getValueStr("path"));


            //params.getDataNode("evaluator").putValue("cities", _cities);

        }
    }

    public Object[][] generateInitialSolutions(int numSolutions) throws Exception
    {
        int numTours = numSolutions;
        int tourLenght = _cities.length;
        Object[][] result = new Object[numTours][];

	Random r = new Random();

        result[0] = TourProvider.createGreedyTour(_cities);
        
        for(int k=1;k<numTours;k++)
        {
            int[] initTour = new int[tourLenght];

            result[k] = new Object[tourLenght];

            for (int i = 0; i < tourLenght; i++)
            {
                int ix = r.nextInt(tourLenght);

                while (initTour[ix] != 0)
                {
                    ix = (ix + 1) % tourLenght;
                }
                initTour[ix] = 1;
                result[k][i] = ix;
            }

        }
        return result;
    }

    public IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception
    {
        String algName = algorithmParams.getName();
        if(algName.equals("geneticAlgorithm"))
            return new TspGeneticAlgorithmFactory();
        if(algName.equals("tabuSearch"))
            return new TspTabuSearchFactory();
        if(algName.equals("simulatedAnnealing"))
            return new TspSimulatedAnnealingFactory(algorithmParams, _cities);
        if(algName.equals("particleSwarm"))
            return new TspParticleSwarmFactory(algorithmParams, _cities);

        throw new Exception("No algorithm factory for name: " + algName);
    }

    public static City[] getCities()
    {
        return _cities;
    }
    
}
