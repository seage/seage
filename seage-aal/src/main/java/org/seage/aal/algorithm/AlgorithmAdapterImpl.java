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
 */
package org.seage.aal.algorithm;

/**
 *
 * @author rick
 */
public abstract class AlgorithmAdapterImpl implements IAlgorithmAdapter
{
    
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
                //System.out.print("+");
            }
        }
        else
            startSearching();
    }
}
