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
    
    public void startSearching(boolean async) throws Exception {
        if(isRunning())
            throw new Exception("Algorithm already running.");
        
        if(async == true)
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
        else
            startSearching();
    }
}
