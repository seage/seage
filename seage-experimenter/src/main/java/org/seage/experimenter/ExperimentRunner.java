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
 *     Richard Malek
 *     - Initial implementation
 *     Jan Zmatlik
 *     - Modified
 */

package org.seage.experimenter;

import java.io.File;

import org.seage.aal.data.ProblemConfig;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
class ExperimentRunner {

    private int _numExperimentAttempts = 5;
    
    public ExperimentRunner() {}
    
    public long run(ProblemConfig config, long timeoutS) throws Exception
    {
        return run(new ProblemConfig[]{config}, timeoutS);
    }

    public long run(ProblemConfig[] configs, long timeoutS) throws Exception
    {
        int ix=0;        
        ExperimentTask[] tasks = new ExperimentTask[configs.length*_numExperimentAttempts];
        long experimentID = System.currentTimeMillis();
        for(int i=0;i<configs.length;i++)
            for(int j=0;j<_numExperimentAttempts;j++)
            {
                tasks[ix++] = new ExperimentTask(experimentID, j+1, timeoutS, configs[i]);
            }

        runRunnableTasks(tasks);
        
        return experimentID;
    }

    public long run(String configPath, long timeoutS) throws Exception
    {
        ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
        return run(config, timeoutS);        
    }

    private void runRunnableTasks(Runnable[] tasks) throws Exception
    {
        int nrOfProcessors = Runtime.getRuntime().availableProcessors();

        int last = 0;
        Thread[] threads = new Thread[nrOfProcessors];

        System.out.println("Tasks: " + tasks.length);
        
        while(true)
        {
            boolean isRunning = false;
            for(int i=0;i<threads.length;i++)
            {
                if(threads[i]==null)
                {
                    if(last < tasks.length)
                    {
                        threads[i] = new Thread(tasks[last++]);
                        threads[i].start();
                        System.out.println("\tTask-"+(last-1)+", Th"+i+"-"+threads[i].toString()+": "+tasks[last-1].toString());
                        isRunning = true;
                    }
                }
                else{
                    if(!threads[i].isAlive())
                        threads[i] = null;
                    else
                    {
                        isRunning = true;
                        System.out.print(".");
                    }
                }
            }
            if(!isRunning && last==tasks.length) break;

            Thread.sleep(500);
        }
    }
}
