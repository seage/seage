/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams;

import org.seage.aal.AlgorithmReport;
import org.seage.aal.AlgorithmReportEvaluator;

/**
 * Reasoner interface hides an implementation of policy evolution
 * @author rick
 */
public abstract class Reasoner
{
    protected Policy.Attribute[] _attributes;
    protected AlgorithmReportEvaluator _runtimeEvaluator;

    public Reasoner(Policy.Attribute[] atts, AlgorithmReportEvaluator re)
    {
        _attributes = atts;
        _runtimeEvaluator = re;
    }

    /**
     * Selects one policy from the policy pool
     * @param none
     * @return Proposed Policy
     * @throws Exception
     */
    public abstract Policy getPolicy() throws Exception;

    /**
     * Puts report on policy quality
     * @param report Report on algorithm run based on policy
     * @throws Exception
     */
    public abstract void putPolicyReport(AlgorithmReport report) throws Exception;
}
