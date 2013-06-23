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
 * 	   - Interface definition
 */
package org.seage.experimenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;

public abstract class Experimenter 
{
	protected static Logger _logger = Logger.getLogger(SingleAlgorithmExperimenter.class.getName());
	protected String _experimentName;
	
	public Experimenter(String experimentName)
	{
		_experimentName = experimentName;
		
		new File("output/experiment-logs").mkdirs();
	}
	
    public void runFromConfigFile(String configPath) throws Exception
    {
    	throw new Exception("Not implemented");
    }

    public void runExperiment( int numOfConfigs, long timeoutS, String problemID) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<String> algIDs = new ArrayList<String>();
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));

        runExperiment(numOfConfigs, timeoutS, problemID, algIDs.toArray(new String[] {}));
    }

    public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        List<String> instanceNames = new ArrayList<String>();
        for (DataNode ins : pi.getDataNode("Instances").getDataNodes("Instance"))
            instanceNames.add(ins.getValueStr("id"));
        
        runExperiment(numOfConfigs, timeoutS, problemID, algorithmIDs, instanceNames.toArray(new String[] {}));
        
    }
    
    public abstract void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception;
}
