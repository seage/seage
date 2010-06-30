/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar;

import java.util.*;

/**
 *
 * @author jenik
   F -> a
 */
public class IntGeneratorFunctor  implements Functor {
                   
    public IntGeneratorFunctor(int startIndex) {
        this.startIndex = startIndex;
    }

    /** @brief retrieve value
      */
    public Object call() {
        if (pos < 0) {
            startIndex %= sourceVector.size();
            pos = sourceVector.get(startIndex) % sourceVector.size();
        }
        if (pos == sourceVector.size())
            pos = 0;
        int len = sourceVector.get(pos) + 1;
        Integer ret = new Integer(0);
        for (int i = 0;i < len;i++) {
            if (pos == sourceVector.size())
                pos = 0;
            ret <<= 8;
            ret += sourceVector.get(pos);
            pos++;            
        }
        return ret;
    }
    
    public void setVector(Vector<Integer> source) {
        this.sourceVector = source;
        this.pos = -1;
    }
    
    private Vector<Integer> sourceVector;
    private int pos = -1;
    private int startIndex = -1;
    
}
