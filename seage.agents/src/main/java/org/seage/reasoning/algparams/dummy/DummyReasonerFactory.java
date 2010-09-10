package org.seage.reasoning.algparams.dummy;

import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import org.seage.aal.AlgorithmReportEvaluator;
import java.util.Vector;

/**
 * @author rick
 */
public class DummyReasonerFactory implements  ReasonerFactory
{
    @Override
    public Reasoner createReasoner(DataNode param) throws Exception
    {
        DataNode reasonerNode = param.getDataNode("dummyReasoner");
        Vector<Attribute> atts = new Vector();

//        for(DataNode node : reasonerNode.getDataNode("parameters").getDataNodes("attr"))
//        {
//            atts.add(new Attribute(node.getValueStr("name"), node.getValueDouble("min"), node.getValueDouble("max")));
//        }

        AlgorithmReportEvaluator re = (AlgorithmReportEvaluator)Class.forName(reasonerNode.getValueStr("runtimeEvaluator")).newInstance();
        
        return new DummyReasoner(atts.toArray(new Attribute[0]), re);
    }
}
