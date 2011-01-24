/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal;

/**
 *
 * @author rick
 */
public class ProblemInstance {
    
    protected String _instanceName;
    
    public ProblemInstance(String instanceName)
    {
        _instanceName = instanceName;
    }

    @Override
    public String toString() {
        return _instanceName;
    }
    
    
}
