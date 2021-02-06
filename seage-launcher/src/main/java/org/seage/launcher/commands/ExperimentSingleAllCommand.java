package org.seage.launcher.commands;

import java.util.Map;
// import java.util.List;
// import java.util.HashMap;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.IProblemProvider;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmExperimenter;
//import org.seage.data.DataNode;



@Parameters(commandDescription = "Perform all single experiment")
public class ExperimentSingleAllCommand extends Command {
    //private static final Logger _logger = LoggerFactory.getLogger(ListCommand.class.getName());

    @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
    int numOfConfigs;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;
    @Parameter(names = "-i", required = false, description = "Instances")
    String instances;

    SingleAlgorithmExperimenter.ConfiguratorType type;

    public ExperimentSingleAllCommand(SingleAlgorithmExperimenter.ConfiguratorType type) {
        this.type = type;
    }

    @Override
    public void performCommad() throws Exception {
        Map<String, IProblemProvider<Phenotype<?>>> problemProviders = ProblemProvider.getProblemProviders();

        // for (String problemId : problemProviders.keySet()) {

        //     try {

        //     IProblemProvider<?> pp = problemProviders.get(problemId);
        //     DataNode pi = pp.getProblemInfo();
            
        //     for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
        //         for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
        //             new SingleAlgorithmRandomExperimenter(problemId, new String[] {inst.getValueStr("id")},
        //                 new String[] {alg.getValueStr("id")}, numOfConfigs, algorithmTimeoutS).runExperiment();
        //             //System.out.println(alg.getValueStr("id") + " - " + inst.getValueStr("id"));
        //         }
        //     }
        //     } catch (Exception ex) {
        //         _logger.error(problemId + ": " + ex.getMessage(), ex);
        //     }
        //     // XmlHelper.writeXml(problems, "problems.xml");
        // }

        for (String problemId : problemProviders.keySet()) {
            // new SingleAlgorithmExperimenter(problemId, new String[] {"-"},
            // new String[] {"-"}, numOfConfigs, algorithmTimeoutS).runExperiment();

            new SingleAlgorithmExperimenter(
                problemId,
                new String[] {"-"},
                new String[] {"-"}, 
                numOfConfigs, 
                algorithmTimeoutS,
                this.type).runExperiment();
        }
  }
}