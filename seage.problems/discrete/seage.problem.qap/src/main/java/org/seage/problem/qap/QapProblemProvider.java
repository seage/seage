/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.qap;

import java.util.Random;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.data.DataNode;
//import org.seage.problem.qap.genetics.QapGeneticAlgorithmFactory;
import org.seage.problem.qap.particles.QapParticleSwarmFactory;
import org.seage.problem.qap.sannealing.QapSimulatedAnnealingFactory;
import org.seage.problem.qap.tabusearch.QapTabuSearchFactory;

/**
 *
 * @author Karel Durkota
 */
public class QapProblemProvider implements IProblemProvider
{
    //private static City[] _cities;

    private static Double[][] _facilityLocation;
    private int currentInstanceIx = -1;

    @Override
    public void initProblemInstance(DataNode params, int instanceIx) throws Exception
    {
        if(currentInstanceIx != instanceIx)
        {
            currentInstanceIx = instanceIx;
            DataNode info = params.getDataNode("instance", instanceIx);
            String path = info.getValueStr("path");

            _facilityLocation = FacilityLocationProvider.readFacilityLocations(info.getValueStr("path"));


            //params.getDataNode("evaluator").putValue("cities", _cities);

        }
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

    @Override
    public IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception
    {
        String algName = algorithmParams.getName();
//        if(algName.equals("geneticAlgorithm"))
//            return new TspGeneticAlgorithmFactory();
        if(algName.equals("tabuSearch"))
            return new QapTabuSearchFactory();
        if(algName.equals("simulatedAnnealing"))
            return new QapSimulatedAnnealingFactory(algorithmParams, _facilityLocation);
        if(algName.equals("particleSwarm"))
            return new QapParticleSwarmFactory(algorithmParams, _facilityLocation);

        throw new Exception("No algorithm factory for name: " + algName);
    }

    @Override
    public void visualize(Object[] solution) throws Exception
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
    
}
