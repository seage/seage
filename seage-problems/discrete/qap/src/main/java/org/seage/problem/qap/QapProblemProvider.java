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

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.Instance;
import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemProvider;

/**
 *
 * @author Karel Durkota
 */
@Annotations.ProblemId("QAP")
@Annotations.ProblemName("Quadratic Assignment Problem")
public class QapProblemProvider extends ProblemProvider
{

    @Override
    public Instance initProblemInstance(InstanceInfo instanceInfo) throws Exception
    {
    	//InstanceInfo instanceInfo = params.getInstanceInfo();
        String type = instanceInfo.getValueStr("type");
        String path = instanceInfo.getValueStr("path");
        //String instanceName = path.substring(path.lastIndexOf('/')+1);
        InputStream stream;            
        if(type.equals("resource"))             
            stream = getClass().getResourceAsStream(path);
        else            
            stream = new FileInputStream(path);

        //params.getDataNode("evaluator").putValue("cities", _cities);
        return new QapProblemInstance(instanceInfo, FacilityLocationProvider.readFacilityLocations(stream));
        
    }

    @Override
    public Object[][] generateInitialSolutions(Instance instance, int numSolutions, long randomSeed) throws Exception
    {
        int numAssigns = numSolutions;
        Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        int assignPrice = facilityLocation[0].length;
        Object[][] result = new Object[numAssigns][assignPrice];

//	Random r = new Random();
        ArrayList<Integer> al = new ArrayList<Integer>();
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
    public void visualizeSolution(Object[] solution, InstanceInfo instance) throws Exception
    {

        // TODO: A - Implement visualize method
//        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
//        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
//        int height = _problemParams.getDataNode("visualizer").getValueInt("height");
//
//        Visualizer.instance().createGraph(_cities, tour, outPath, width, height);
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator(Instance instance) throws Exception {
        return new QapPhenotypeEvaluator((QapProblemInstance)instance);
    }


    
}
