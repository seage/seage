package org.seage.launcher.commands;

import java.util.List;

import org.seage.experimenter.multialgorithm.MultiAlgorithmRandomExperimenter;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters( commandDescription = "Perform multi random experiment")
public class ExperimentMultiRandomCommand extends Command
{
    @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
    int numOfConfigs;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;
    @Parameter(names = "-p", required = true, description = "ProblemID")
    String problemID;
    @Parameter(names = "-i", required = true, description = "Problem instances", variableArity = true)
    List<String> instances;
    @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
    List<String> algorithms;
    @Override
    public void performCommad() throws Exception
    {
//        new MultiAlgorithmRandomExperimenter("")
//            .runExperiment(problemID, instances.toArray(new String[]{}), algorithms.toArray(new String[]{}));  
    }

}
