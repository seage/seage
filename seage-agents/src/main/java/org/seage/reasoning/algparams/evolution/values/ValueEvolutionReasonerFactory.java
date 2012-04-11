package org.seage.reasoning.algparams.evolution.values;

import org.seage.aal.reporter.AlgorithmReportEvaluator;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import java.util.Vector;

/**
 *
 * @author rick
 */
public class ValueEvolutionReasonerFactory implements ReasonerFactory
{

    public Reasoner createReasoner(DataNode param) throws Exception 
    {
        Vector<String> names = new Vector<String>();
        Vector<Attribute> atts = new Vector<Attribute>();
        for(DataNode dn : param.getDataNodes("attr"))
        {
            names.add(dn.getValueStr("name"));
            atts.add(new Attribute(dn.getValueStr("name"), dn.getValueDouble("min"), dn.getValueDouble("max")));
        }

        AlgorithmReportEvaluator re = (AlgorithmReportEvaluator)Class.forName(param.getValueStr("runtimeEvaluator")).newInstance();

        return new ValueEvolutionReasoner(atts.toArray(new Attribute[]{}), re);
    }

}
