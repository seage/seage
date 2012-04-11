/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.algorithm.sannealing;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReportEvaluator;

/**
 *
 * @author rick
 */
public class SimulatedAnnealingRuntimeEvaluator extends AlgorithmReportEvaluator{

    @Override
    public int evaluate(AlgorithmReport statistics) {
        return 0;
    }

    @Override
    public int compare(AlgorithmReport o1, AlgorithmReport o2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
