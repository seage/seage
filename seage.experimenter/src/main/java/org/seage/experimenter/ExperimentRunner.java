/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.experimenter;

import java.io.File;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
class ExperimentRunner {

    private static int _numExperimentAttempts = 5;
    private ExperimentRunner(){}
    
    public static void run(ProblemConfig config, long timeoutS) throws Exception
    {
        runRunnableTasks(new Runnable[]{new ExperimentTask(config, timeoutS)});
    }

    public static void run(ProblemConfig[] configs, long timeoutS) throws Exception
    {
        int ix=0;
        long runID = System.currentTimeMillis();
        ExperimentTask[] tasks = new ExperimentTask[configs.length*_numExperimentAttempts];
        for(int i=0;i<configs.length;i++)
            for(int j=0;j<_numExperimentAttempts;j++)
            {
                configs[i].putValue("runID", runID);
                tasks[ix++] = new ExperimentTask(configs[i], timeoutS);
            }

        runRunnableTasks(tasks);
    }

    public static void run(String configPath, long timeoutS) throws Exception
    {
        ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
        run(config, timeoutS);
    }

    private static void runRunnableTasks(Runnable[] tasks) throws Exception
    {
        int nrOfProcessors = Runtime.getRuntime().availableProcessors();

        int last = 0;
        Thread[] threads = new Thread[nrOfProcessors];

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
                        System.out.println(threads[i].toString()+": "+tasks[last-1].toString());
                        isRunning = true;
                    }
                }
                else{
                    if(!threads[i].isAlive())
                        threads[i] = null;
                    else
                        isRunning = true;
                }
            }
            if(!isRunning && last==tasks.length) break;

            Thread.sleep(500);
        }
    }
}
