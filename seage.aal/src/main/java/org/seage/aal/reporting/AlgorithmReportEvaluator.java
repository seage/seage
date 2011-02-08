/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.reporting;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author rick
 */
public abstract class AlgorithmReportEvaluator implements Serializable, Comparator<AlgorithmReport>
{


    /**
     * Evaluates statistics of previous algorithm run
     * @param statistics
     * @return A number between 0 and 100 (0 is the best, 100 is the worst)
     */
    public abstract int evaluate(AlgorithmReport statistics);
    
    @Override
    public abstract int compare(AlgorithmReport o1, AlgorithmReport o2);
}
