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

import java.util.Random;
import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;


/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("tsp")
@Annotations.ProblemName("Travelling Salesman Problem")
public class TspProblemProvider extends ProblemProvider
{
    private static City[] _cities;
    private int currentInstanceIx = -1;

    @Override
    public void initProblemInstance(DataNode params) throws Exception
    {
        if(currentInstanceIx != 0)
        {
            currentInstanceIx = 0;
            DataNode info = params.getDataNode("instance", 0);
            String path = info.getValueStr("path");

            if(path.equals("[circle]"))
                _cities = CityProvider.generateCircleCities(info.getValueInt("numberOfCities"));
            else
                _cities = CityProvider.readCities(info.getValueStr("path"));


            //params.getDataNode("evaluator").putValue("cities", _cities);

        }
    }

    @Override
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
    public void visualizeSolution(Object[] solution) throws Exception
    {
        Integer[] tour = (Integer[])solution;

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public static City[] getCities()
    {
        return _cities;
    }

    public IAlgorithmAdapter initAlgorithm(DataNode params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
}
