package org.seage.reasoning.algparams.dummy;

import org.seage.aal.reporting.AlgorithmReport;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.aal.reporting.AlgorithmReportEvaluator;

public class DummyReasoner extends  Reasoner
{
    public DummyReasoner(Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
    }

    /**
     * Selects one policy from the policy pool
     * @param none
     * @return Proposed Policy    
     */
    public Policy getPolicy ()
    {
        return new DummyPolicy();
    }

    /**
     * Puts report on policy quality
     * @param report Report on algorithm run based on policy    
     */
    public void putPolicyReport (AlgorithmReport report)
    {
        // DummyReasoner doesn't take a report back.
    }

}

