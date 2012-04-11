/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.aal.algorithm;

/**
 *
 * @author rick
 */
public abstract class AlgorithmAdapterImpl implements IAlgorithmAdapter{
    
    protected boolean _algorithmStarted = false;
    protected boolean _algorithmStopped = false;
    
    public void startSearching(boolean async) throws Exception {
        if(_algorithmStarted && !_algorithmStopped)
            throw new Exception("Algorithm already started, running.");
        
        _algorithmStarted = false;
        
        if(async == true)
        {
            
            new Thread(new Runnable() {
                public void run() {
                    try
                    {
                        startSearching();                        
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }).start();

            while(!_algorithmStarted)
            {
                Thread.sleep(100);
                System.out.print("+");
            }
        }
        else
            startSearching();
    }
}
