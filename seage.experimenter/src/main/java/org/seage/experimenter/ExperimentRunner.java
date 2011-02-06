/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private ExperimentRunner(){}
    
    public static void run(ProblemConfig config) throws Exception
    {
        runRunnableTasks(new Runnable[]{new ExperimentTask(config)});
    }

    public static void run(ProblemConfig[] configs) throws Exception
    {
        ExperimentTask[] tasks = new ExperimentTask[configs.length];
        for(int i=0;i<configs.length;i++)
            tasks[i] = new ExperimentTask(configs[i]);

        runRunnableTasks(tasks);
    }

    public static void run(String configPath) throws Exception
    {
        ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
        run(config);
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
            if(!isRunning) break;

            Thread.sleep(500);
        }
    }
}
