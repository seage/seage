/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.dummyrandom;

import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import org.seage.aal.reporting.AlgorithmReportEvaluator;
import java.util.Vector;

/**
 *
 * @author rick
 */
public class DummyRandomReasonerFactory implements  ReasonerFactory{

    public DummyRandomReasonerFactory() {
    }

    @Override
    public Reasoner createReasoner(DataNode param) throws Exception
    {
        Vector<Attribute> atts = new Vector();

        for(DataNode node : param.getDataNodes("attr"))
        {
            atts.add(new Attribute(node.getValueStr("name"), node.getValueDouble("min"), node.getValueDouble("max")));
        }

        AlgorithmReportEvaluator re = (AlgorithmReportEvaluator)Class.forName(param.getDataNode("randomReasoner").getValueStr("runtimeEvaluator")).newInstance();

        return new DummyRandomReasoner(atts.toArray(new Attribute[0]), re);
    }

}
