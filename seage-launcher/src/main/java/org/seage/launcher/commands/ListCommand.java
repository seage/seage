package org.seage.launcher.commands;

import java.util.Map;

import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "List implemented problems and algorithms")
public class ListCommand extends Command 
{
    private static final Logger _logger = LoggerFactory.getLogger(ListCommand.class.getName());
    @Override
    public void performCommad() throws Exception 
    {
        _logger.info("List of implemented problems and algorithms:");
        _logger.info("--------------------------------------------");

        DataNode problems = new DataNode("Problems");
        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for (String problemId : providers.keySet())
        {
            try
            {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                problems.putDataNode(pi);

                String name = pi.getValueStr("name");
                _logger.info(problemId + " - " + name);

                _logger.info("\talgorithms:");
                for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
                {
                    _logger.info("\t\t" + alg.getValueStr("id")/*
                                                               * +" ("+alg.getValueStr("id")+")"
                                                               */);

                    //_logger.info("\t\t\tparameters:");
                    for (DataNode param : alg.getDataNodes("Parameter"))
                    {
                        _logger.info("\t\t\t"
                                + param.getValueStr("name") + "  ("
                                + param.getValueStr("min") + ", "
                                + param.getValueStr("max") + ", "
                                + param.getValueStr("init") + ")");
                    }
                }
                _logger.info("\tinstances:");
                for (DataNode inst : pi.getDataNode("Instances").getDataNodes())
                {
                    _logger.info("\t\t" + inst.getValueStr("type") + "=" + inst.getValueStr("path")/*
                                                                                                   * +" ("+alg.getValueStr("id")+")"
                                                                                                   */);
                }

                _logger.info("");
            }
            catch (Exception ex)
            {
                _logger.error( problemId + ": " + ex.getMessage(), ex);
            }
            //XmlHelper.writeXml(problems, "problems.xml");
        }   }
}
