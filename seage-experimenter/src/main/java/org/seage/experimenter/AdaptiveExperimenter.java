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

import org.seage.data.DataNode;
import org.seage.experimenter.config.RandomConfiguratorEx;

public class AdaptiveExperimenter implements IExperimenter {
    
    public AdaptiveExperimenter()
    {
    }
    
	@Override
	public void runFromConfigFile(String configPath) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void runExperiments(String problemID, int numRuns, long timeoutS)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void runExperiments(String problemID, int numRuns, long timeoutS,
			String[] algorithmIDs) throws Exception 
	{
		DataNode stats = null;
		for(int i=0;i<5/*10,15 ?*/;i++)
		{			
			RandomConfiguratorEx configurator = new RandomConfiguratorEx(stats);
			
			// run experiments
			// ...
			
			stats = null; // getStatistics(...);
		}
		
		// generate report

	}

}
