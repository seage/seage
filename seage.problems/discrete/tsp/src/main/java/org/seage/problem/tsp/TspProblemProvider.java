/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
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
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.ProblemInstance;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;


/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("TSP")
@Annotations.ProblemName("Travelling Salesman Problem")
public class TspProblemProvider extends ProblemProvider
{

    @Override
    public ProblemInstance initProblemInstance(DataNode params) throws Exception
    {
        City[] cities;
        DataNode info = params.getDataNode("Instance", 0);
        String type = info.getValueStr("type");

//            if(path.equals("[circle]"))
//                _cities = CityProvider.generateCircleCities(info.getValueInt("numberOfCities"));
//            else
//                _cities = CityProvider.readCities(info.getValueStr("path"));
        InputStream stream;
        if(type.equals("resource"))        
            stream = getClass().getResourceAsStream(info.getValueStr("path"));
        else
            stream = new FileInputStream(info.getValueStr("path")); 

        //params.getDataNode("evaluator").putValue("cities", _cities);
        return new TspProblemInstance(CityProvider.readCities(stream));
 
    }

    @Override
    public Object[][] generateInitialSolutions(int numSolutions, ProblemInstance instance) throws Exception
    {
        int numTours = numSolutions;
        City[] cities = ((TspProblemInstance)instance).getCities();
        int tourLenght = cities.length;
        Object[][] result = new Object[numTours][];

	Random r = new Random();

        result[0] = TourProvider.createGreedyTour(cities);
        
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
    public void visualizeSolution(Object[] solution, ProblemInstance instance) throws Exception
    {
        Integer[] tour = (Integer[])solution;

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
}
