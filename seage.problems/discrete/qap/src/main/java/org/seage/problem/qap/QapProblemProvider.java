/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added problem annotations
 */

package org.seage.problem.qap;

import java.util.Random;
import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;

/**
 *
 * @author Karel Durkota
 */
@Annotations.ProblemId("qap")
@Annotations.ProblemName("Quadratic Assignment Problem")
public class QapProblemProvider extends ProblemProvider
{
    //private static City[] _cities;

    private static Double[][] _facilityLocation;
    private int currentInstanceIx = -1;

    @Override
    public void initProblemInstance(DataNode params) throws Exception
    {
//        if(currentInstanceIx != instanceIx)
//        {
//            currentInstanceIx = instanceIx;
//            DataNode info = params.getDataNode("instance", instanceIx);
//            String path = info.getValueStr("path");
//
//            _facilityLocation = FacilityLocationProvider.readFacilityLocations(info.getValueStr("path"));
//
//
//            //params.getDataNode("evaluator").putValue("cities", _cities);
//
//        }
    }

    @Override
    public Object[][] generateInitialSolutions(int numSolutions) throws Exception
    {
        int numAssigns = numSolutions;
        int assignPrice = _facilityLocation.length;
        Object[][] result = new Object[numAssigns][];

	Random r = new Random();

        result[0] = AssignmentProvider.createGreedyAssignment(_facilityLocation);
        
        for(int k=1;k<numAssigns;k++)
        {
            int[] initAssign = new int[assignPrice];

            result[k] = new Object[assignPrice];

            for (int i = 0; i < assignPrice; i++)
            {
                int ix = r.nextInt(assignPrice);

                while (initAssign[ix] != 0)
                {
                    ix = (ix + 1) % assignPrice;
                }
                initAssign[ix] = 1;
                result[k][i] = ix;
            }

        }
        return result;
    }

//    @Override
//    public IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception
//    {
//        String algName = algorithmParams.getName();
////        if(algName.equals("geneticAlgorithm"))
////            return new TspGeneticAlgorithmFactory();
//        if(algName.equals("tabuSearch"))
//            return new QapTabuSearchFactory();
//        if(algName.equals("simulatedAnnealing"))
//            return new QapSimulatedAnnealingFactory(algorithmParams, _facilityLocation);
//        if(algName.equals("particleSwarm"))
//            return new QapParticleSwarmFactory(algorithmParams, _facilityLocation);
//
//        throw new Exception("No algorithm factory for name: " + algName);
//    }

    @Override
    public void visualizeSolution(Object[] solution) throws Exception
    {
        Integer[] assign = (Integer[])solution;

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public static Double[][] getFacilityLocation()
    {
        return _facilityLocation;
    }

    public IAlgorithmAdapter initAlgorithm(DataNode params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
}
