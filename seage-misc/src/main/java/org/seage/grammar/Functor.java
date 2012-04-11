/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.grammar;

import java.util.*;

/**
 *
 * @author jenik
   F -> a
 */
public interface Functor  {
               
    /** @brief retrieve value */      
    abstract public Object call();
    
    abstract public void setVector(Vector<Integer> source);
    
}
