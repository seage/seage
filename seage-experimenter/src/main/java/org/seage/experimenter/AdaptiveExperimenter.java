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
 * Contributors:
 * 	   Richard Malek
 * 	   - Created the class
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.config.RandomConfigurator;
import org.seage.experimenter.config.RandomConfiguratorEx;
import org.seage.experimenter.reporting.LogReportCreator;
import org.seage.experimenter.reporting.rapidminer.ProcessPerformer;
import org.seage.experimenter.reporting.rapidminer.RMProcess;

public class AdaptiveExperimenter implements IExperimenter {
    
    Configurator _configurator;
    Configurator _configuratorAdaptive;
    ProcessPerformer _processPerformer;
    ExperimentRunner _experimentRunner;
    private long _experimentID;
    
    public AdaptiveExperimenter ()throws Exception
    {
        //########### BASIC EXPERIMENTER ############
        _configurator = new RandomConfigurator();
        _experimentRunner = new ExperimentRunner();
        //########### BASIC EXPERIMENTER ############
        
        _processPerformer = new ProcessPerformer();
    }
    
    @Override
    public void runFromConfigFile(String configPath) throws Exception
    {
        _experimentID = System.currentTimeMillis();
        //########### BASIC EXPERIMENTER ############
        _experimentRunner.run(configPath, Long.MAX_VALUE, _experimentID);
        //########### BASIC EXPERIMENTER ############
    }
    
    @Override
    public void runExperiments(String problemID, int numRuns, long timeoutS) throws Exception
    {
        //########### BASIC EXPERIMENTER ############
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        List<String> algIDs = new ArrayList<String>();
        for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));
        
        runExperiments(problemID, numRuns, timeoutS, algIDs.toArray(new String[]{}));        
        //########### BASIC EXPERIMENTER ############
    }

    @Override
    public void runExperiments(String problemID, int numRuns, long timeoutS,
                    String[] algorithmIDs) throws Exception 
    {
        //########### BASIC EXPERIMENTER ############
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
        for(String algID : algorithmIDs)
            configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, numRuns)));
                
        _experimentID = System.currentTimeMillis();
        _experimentRunner.run(configs.toArray(new ProblemConfig[]{}), timeoutS, _experimentID);
        //########### BASIC EXPERIMENTER ############
        
        LogReportCreator reporter = new LogReportCreator();
        reporter.report( String.valueOf( _experimentID ) );
        
        RMProcess process = new RMProcess("rm-report1-p4.rmp", "AggregateOutput", "Report 1", Arrays.asList( "example set output" ));
        _processPerformer.addProcess( process );
        _processPerformer.performProcesses();
        
        List<DataNode> dataNodes = _processPerformer.getProcessesDataNodes();
        
        _configuratorAdaptive = new RandomConfiguratorEx( dataNodes.get(0) );
        
        configs = new ArrayList<ProblemConfig>();
        
        for(String algID : algorithmIDs)
            configs.addAll(Arrays.asList(_configuratorAdaptive.prepareConfigs(pi, algID, numRuns)));
        
        _experimentID = System.currentTimeMillis();
        _experimentRunner.run(configs.toArray(new ProblemConfig[]{}), timeoutS, _experimentID);
    }

}
