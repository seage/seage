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
 */
package org.seage.problem.tsp;

import java.util.Random;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.data.DataNode;
import org.seage.problem.IProblemSolver;
import org.seage.problem.tsp.data.City;
import org.seage.problem.tsp.data.CityProvider;
import org.seage.problem.tsp.data.Visualizer;
import org.seage.problem.tsp.genetics.TspGeneticAlgorithmFactory;
import org.seage.problem.tsp.sannealing.TspSimulatedAnnealingFactory;
import org.seage.problem.tsp.tabusearch.TspTabuSearchFactory;

/**
 *
 * @author Richard Malek
 */
public class TspProblemSolver implements IProblemSolver
{
    private City[] _cities;
    private IAlgorithmAdapter _algorithm;
    private DataNode _problemParams;
    private DataNode _algorithmParams;

    public TspProblemSolver(DataNode config) throws Exception
    {
        _problemParams = config.getDataNode("problem");
        _cities = CityProvider.readCities(
                 config.getDataNode("problem").getDataNode("instance").getValueStr("path"));
        _algorithm = createFactory(config).createAlgorithm();
    }

    public void runAlgorithm() throws Exception
    {
        _algorithm.startSearching(_algorithmParams);
    }

    public void reportBest() throws Exception
    {
        System.out.println("Best: "+_algorithm.getReport().getDataNode("statistics").getValueStr("bestObjVal"));
    }

    public void visualize() throws Exception
    {
        Integer[] tour = (Integer[])_algorithm.solutionsToPhenotype()[0];

        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
        int height = _problemParams.getDataNode("visualizer").getValueInt("height");

        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    private IAlgorithmFactory createFactory(DataNode config) throws Exception
    {
        _algorithmParams = config.getDataNode(config.getDataNode("problem").getValueStr("runAlgorithm"));
        String algName = _algorithmParams.getName();
        
        if(algName.equals("algGeneticAlgorithm"))
            return new TspGeneticAlgorithmFactory(_algorithmParams, _cities);
        if(algName.equals("algTabuSearch"))
            return new TspTabuSearchFactory(_algorithmParams, _cities);
        if(algName.equals("algSimulatedAnnealing"))
            return new TspSimulatedAnnealingFactory(_algorithmParams, _cities);

        throw new Exception("No algorithm factory for name: " + algName);
    }

    public static Integer[][] generateInitialSolutions(int length, int count )
    {
        int numTours = count;
        int tourLenght = length;
        Integer[][] result = new Integer[numTours][];

        Random r = new Random();

        for(int k=0;k<numTours;k++)
        {
            result[k] = new Integer[tourLenght];

            for (int i = 0; i < tourLenght; i++)
            {
                result[k][i] = i;
            }

            for (int i = 0; i < tourLenght*10; i++)
            {
                int a = r.nextInt(length);
                int b = r.nextInt(length);
                // swap
                int t = result[k][a];
                result[k][a] = result[k][b];
                result[k][b] = t;
            }
        }
        return result;
    }

    
}
