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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.data.DataNode;

/**
 *
 * @author Karel Durkota
 */
@Annotations.ProblemId("QAP")
@Annotations.ProblemName("Quadratic Assignment Problem")
public class QapProblemProvider extends ProblemProvider
{

    @Override
    public ProblemInstanceInfo initProblemInstance(ProblemConfig params) throws Exception
    {
        DataNode info = params.getDataNode("Problem").getDataNode("Instance", 0);
        String type = info.getValueStr("type");
        String path = info.getValueStr("path");
        String instanceName = path.substring(path.lastIndexOf('/')+1);
        InputStream stream;            
        if(type.equals("resource"))             
            stream = getClass().getResourceAsStream(path);
        else            
            stream = new FileInputStream(path);


        //params.getDataNode("evaluator").putValue("cities", _cities);
        return new QapProblemInstance(instanceName, FacilityLocationProvider.readFacilityLocations(stream));
        
    }

    @Override
    public Object[][] generateInitialSolutions(int numSolutions, ProblemInstanceInfo instance) throws Exception
    {
        int numAssigns = numSolutions;
        Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        int assignPrice = facilityLocation[0].length;
        Object[][] result = new Object[numAssigns][assignPrice];

//	Random r = new Random();
        ArrayList al = new ArrayList();
        for(int i=0;i<assignPrice;i++)
            al.add(i);
        for(int i=0;i<numAssigns;i++){
            Collections.shuffle(al);
            result[i]=al.toArray();
        }
        return result;
//        Object result[][]=new Object[numAssigns][assignPrice];


//        result[0] = AssignmentProvider.createGreedyAssignment(facilityLocation);
//
//        for(int k=1;k<numAssigns;k++)
//        {
//            int[] initAssign = new int[assignPrice];
//
//            result[k] = new Object[assignPrice];
//
//            for (int i = 0; i < assignPrice; i++)
//            {
//                int ix = r.nextInt(assignPrice);
//
//                while (initAssign[ix] != 0)
//                {
//                    ix = (ix + 1) % assignPrice;
//                }
//                initAssign[ix] = 1;
//                result[k][i] = ix;
//            }
//
//        }
//        return result;
    }

//    @Override
//    public IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception
//    {
//        String algName = algorithmParams.getName();
////        if(algName.equals("geneticAlgorithm"))
////           return new QapGeneticAlgorithmFactory();
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
    public void visualizeSolution(Object[] solution, ProblemInstanceInfo instance) throws Exception
    {
        Integer[] assign = (Integer[])solution;

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        return new QapPhenotypeEvaluator();
    }


    
}
