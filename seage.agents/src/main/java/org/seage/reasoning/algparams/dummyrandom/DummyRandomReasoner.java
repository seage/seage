/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.dummyrandom;

import org.seage.aal.AlgorithmReport;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.aal.AlgorithmReportEvaluator;

/**
 *
 * @author rick
 */
public class DummyRandomReasoner extends  Reasoner
{

    public DummyRandomReasoner(Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
        _attributes = atts;
        _runtimeEvaluator = re;
    }

    public Policy getPolicy()
    {
        return new DummyRandomPolicy(_attributes, _runtimeEvaluator);
    }

    public void putPolicyReport(AlgorithmReport report)
    {
        // throw new UnsupportedOperationException("This method should not have been invoked.");
    }

}
