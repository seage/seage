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
 * 	   Richard Malek
 * 	   - Created the class
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter.singlealgorithm.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.ExperimentRunner;
import org.seage.experimenter.IExperimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.config.RandomConfigurator;
import org.seage.experimenter.config.RandomConfiguratorEx;
import org.seage.experimenter.reporting.LogReportCreator;
import org.seage.experimenter.reporting.rapidminer.old.ProcessPerformer;
import org.seage.experimenter.reporting.rapidminer.old.RMProcess;

public class AdaptiveExperimenter implements IExperimenter
{
    private static final String PARAM_INTERVALS = "ParamIntervals";
    private static final String ALGORITHMS = "Algorithms";
    private static final String ALGORITHM = "Algorithm";
    private static final String PARAMETER = "Parameter";
    private static final String EXAMPLE_SET = "ExampleSet";
    private static final String EXAMPLES = "Examples";
    private static final String EXAMPLE = "Example";
    private static final String RESULT = "Result";

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_MIN = "min";
    private static final String ATTRIBUTE_MAX = "max";
    private static final String ATTRIBUTE_AVG = "avg";
    private static final String ATTRIBUTE_DEV = "dev";

    private static final String REPORT_FILENAME = "rm-report1-p4.rmp";
    private static final String REPORT_OPERATORNAME = "AggregateOutput";
    private static final String REPORT_NAME = "Report 1";
    private static final String ATTRIBUTE_OUTPUT_1 = "result 1";
    private static final String ATTRIBUTE_OUTPUT_2 = "result 2";

    Configurator _configurator;
    Configurator _configuratorAdaptive;
    ProcessPerformer _processPerformer;
    ExperimentRunner _experimentRunner;
    private long _experimentID;

    public AdaptiveExperimenter() throws Exception
    {
        // ########### BASIC EXPERIMENTER ############
        _configurator = new RandomConfigurator();
        _experimentRunner = new ExperimentRunner();
        // ########### BASIC EXPERIMENTER ############

        _processPerformer = new ProcessPerformer();
    }

    @Override
    public void runFromConfigFile(String configPath) throws Exception
    {
        // ########### BASIC EXPERIMENTER ############
        _experimentRunner.run(configPath, Long.MAX_VALUE);
        // ########### BASIC EXPERIMENTER ############
    }

    @Override
    public void runExperiment(int numOfConfigs, long timeoutS, String problemID ) throws Exception
    {
        // ########### BASIC EXPERIMENTER ############
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<String> algIDs = new ArrayList<String>();
        for (DataNode alg : pi.getDataNode(ALGORITHMS).getDataNodes(ALGORITHM))
            algIDs.add(alg.getValueStr(ATTRIBUTE_ID));

        runExperiment(numOfConfigs, timeoutS, problemID, algIDs.toArray(new String[] {}));
        // ########### BASIC EXPERIMENTER ############
    }

    @Override
    public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs) throws Exception
    {
        long experimentID = System.currentTimeMillis();
        // ########### BASIC EXPERIMENTER ############
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
        for (String algID : algorithmIDs)
            configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, numOfConfigs)));

        _experimentID = _experimentRunner.run(configs.toArray(new ProblemConfig[] {}), "Adaptive", experimentID, problemID, "", "", timeoutS);
        // ########### BASIC EXPERIMENTER ############

        LogReportCreator reporter = new LogReportCreator();
        reporter.report(String.valueOf(_experimentID));

        RMProcess process = new RMProcess(REPORT_FILENAME, REPORT_OPERATORNAME, REPORT_NAME, Arrays.asList(ATTRIBUTE_OUTPUT_1, ATTRIBUTE_OUTPUT_2));
        _processPerformer.addProcess(process);
        _processPerformer.performProcessThroughSinks(process);

        List<DataNode> dataNodes = _processPerformer.getProcessesDataNodes();

        _configuratorAdaptive = new RandomConfiguratorEx(dataNodes.get(0));

        configs = new ArrayList<ProblemConfig>();

        for (String algID : algorithmIDs)
        {
            DataNode paramIntervals = prepareDataForConfig(pi, dataNodes.get(0), algID);
            configs.addAll(Arrays.asList(((RandomConfiguratorEx) _configuratorAdaptive).prepareConfigs(paramIntervals, pi, algID, numOfConfigs)));
        }

        _experimentRunner.run(configs.toArray(new ProblemConfig[] {}), "Adaptive", experimentID, problemID, "", "", timeoutS);
    }

    /**
     * @param ProblemInfo
     *            problemInfo - Instance of ProblemInfo
     * @param DataNode
     *            statistics - statistics from RapidMiner process
     * @param String
     *            algID - id of algortigm
     * 
     * @return DataNode - method return a DataNode with structure
     * 
     *         ParamIntervals - Parameter: avg, dev, min, max, name attributes
     * 
     **/
    private DataNode prepareDataForConfig(ProblemInfo problemInfo, DataNode statistics, String algID) throws Exception
    {
        DataNode paramIntervals = new DataNode(PARAM_INTERVALS);

        int i = 0;
        for (DataNode paramNode : problemInfo.getDataNode(ALGORITHMS).getDataNodeById(algID).getDataNodes(PARAMETER))
        {
            DataNode param = new DataNode(PARAMETER);
            String parameterName = paramNode.getValueStr(ATTRIBUTE_NAME);

            double avgValue = statistics.getDataNode(EXAMPLE_SET, 0).getDataNode(EXAMPLES).getDataNode(EXAMPLE).getDataNode(RESULT, i)
                    .getValueDouble(ATTRIBUTE_VALUE);
            double devValue = statistics.getDataNode(EXAMPLE_SET, 1).getDataNode(EXAMPLES).getDataNode(EXAMPLE).getDataNode(RESULT, i)
                    .getValueDouble(ATTRIBUTE_VALUE);

            param.putValue(ATTRIBUTE_NAME, parameterName);
            param.putValue(ATTRIBUTE_MIN, avgValue - devValue);
            param.putValue(ATTRIBUTE_MAX, avgValue + devValue);
            param.putValue(ATTRIBUTE_AVG, avgValue);
            param.putValue(ATTRIBUTE_DEV, devValue);

            paramIntervals.putDataNode(param);
            i++;
        }
        return paramIntervals;
    }

	@Override
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}
