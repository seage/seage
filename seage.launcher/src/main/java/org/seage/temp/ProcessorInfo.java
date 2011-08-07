/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.temp;

public class ProcessorInfo {

    /**
     * Displays the number of processors available in the Java Virtual Machine
     */
    public void displayAvailableProcessors() {

        Runtime runtime = Runtime.getRuntime();

        int nrOfProcessors = runtime.availableProcessors();

        System.out.println("Number of processors available to the Java Virtual Machine: " + nrOfProcessors);

    }

    private class Experiment
    {
        private int _time;

        public Experiment()
        {
            _time = (int)((Math.random() * 10000) + 5000);
        }

        public int getTime()
        {
            return _time;
        }
    }
    
    private class Worker implements Runnable
    {
        private Experiment _exp;
        public Worker(Experiment exp)
        {
            _exp = exp;
        }

        @Override
        public void run() {
            
            //System.out.println("Running "+getName()+" for " + _exp.getTime() +" ms");
            System.out.println("Running a task for " + _exp.getTime() +" ms");
            long t1 = System.currentTimeMillis();
            while(System.currentTimeMillis() - t1 < _exp.getTime()){}
            System.out.println("Stopping a task");
            //System.out.println(getName()+" done");
        }
        
    }

    public void runRunnableTasks(Runnable[] tasks) throws Exception
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
    
    public void foo() throws  Exception
    {

        Runnable[] w = new Worker[10];
        for(int i=0;i<w.length;i++)
            w[i] = new Worker(new Experiment());

        runRunnableTasks(w);
    }
    /**
     * Starts the program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            ProcessorInfo pi = new ProcessorInfo();
            pi.displayAvailableProcessors();             
            pi.foo();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}