/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     - Added problem annotations
 */
package org.seage.problem.tsp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.Instance;
import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemProvider;


/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("TSP")
@Annotations.ProblemName("Travelling Salesman Problem")
public class TspProblemProvider extends ProblemProvider
{

    @Override
    public Instance initProblemInstance(InstanceInfo instanceInfo) throws Exception
    {
        City[] cities;        
        
        String type = instanceInfo.getValueStr("type");
        String path = instanceInfo.getValueStr("path");

        InputStream stream;
        if(type.equals("resource"))        
            stream = getClass().getResourceAsStream(path);
        else
            stream = new FileInputStream(path); 

        try{
            cities = CityProvider.readCities(stream);
        }catch(Exception ex){
            System.err.println("TspProblemProvider.initProblemInstance - readCities failed, path: " + path);
            throw ex;
        }

        return new TspProblemInstance(instanceInfo, cities);
 
    }

    @Override
    public Object[][] generateInitialSolutions(Instance instance, int numSolutions, long randomSeed) throws Exception
    {
        int numTours = numSolutions;
        City[] cities = ((TspProblemInstance)instance).getCities();
        int tourLenght = cities.length;
        Integer[][] result = new Integer[numTours][];

        Random r = new Random(randomSeed);

        result[0] = TourProvider.createGreedyTour(cities, randomSeed);
        
        for(int k=1;k<numTours;k++)
        {
            int[] initTour = new int[tourLenght];

            result[k] = new Integer[tourLenght];

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

//    @Override
//    public IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception
//    {
//        String algName = algorithmParams.getName();
//        if(algName.equals("geneticAlgorithm"))
//            return new TspGeneticAlgorithmFactory();
//        if(algName.equals("tabuSearch"))
//            return new TspTabuSearchFactory();
//        if(algName.equals("simulatedAnnealing"))
//            return new TspSimulatedAnnealingFactory(algorithmParams, _cities);
//        if(algName.equals("particleSwarm"))
//            return new TspParticleSwarmFactory(algorithmParams, _cities);
//
//        throw new Exception("No algorithm factory for name: " + algName);
//    }

    @Override
    public void visualizeSolution(Object[] solution, InstanceInfo instance) throws Exception
    {
//        Integer[] tour = (Integer[])solution;

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator(Instance instance) throws Exception {
        return new TspPhenotypeEvaluator((TspProblemInstance)instance);
    }

}
