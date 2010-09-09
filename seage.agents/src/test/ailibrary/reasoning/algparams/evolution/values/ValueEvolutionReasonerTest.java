/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.reasoning.algparams.evolution.values;

import org.seage.reasoning.algparams.evolution.values.ValueEvolutionReasonerFactory;
import org.seage.aal.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rick
 */
public class ValueEvolutionReasonerTest
{
    private Reasoner _reasoner;

    public ValueEvolutionReasonerTest() throws Exception
    {
        ReasonerFactory factory = new ValueEvolutionReasonerFactory();

        DataNode dn = new DataNode("reasoner");
        dn.putValue("runtimeEvaluator", "ailibrary.aal.algorithm.genetics.GeneticSearchRuntimeEvaluator");
        DataNode att = null;
        
        att = new DataNode("attr");
        att.putValue("name", "p1");
        att.putValue("min", 1);
        att.putValue("max", 100);
        dn.putDataNode(att);

        att = new DataNode("attr");
        att.putValue("name", "p2");
        att.putValue("min", 1);
        att.putValue("max", 100);
        dn.putDataNode(att);

        att = new DataNode("attr");
        att.putValue("name", "p3");
        att.putValue("min", 1);
        att.putValue("max", 100);
        dn.putDataNode(att);

        att = new DataNode("attr");
        att.putValue("name", "p4");
        att.putValue("min", 1);
        att.putValue("max", 100);
        dn.putDataNode(att);

        _reasoner = factory.createReasoner(dn);
    }

    /**
     * Test of createPolicy method, of class ValueEvolutionReasoner.
     */
    @Test
    public void testCreatePolicy() throws Exception
    {
        System.out.println("createPolicy");        

        for(int i=0;i<20;i++)
        {
            Policy p1 = _reasoner.getPolicy();
            assertNotNull(p1);
        }

        AlgorithmReport rr = new AlgorithmReport("report");
        rr.putDataNode(new DataNode("statistics"));

        for(int i=0;i<20;i++)
        {
           rr.putValue("id", i+1);
            rr.getDataNode("statistics").putValue("numberOfNewSolutions", Math.random()*25);
            _reasoner.putPolicyReport(rr);
        }
        
        Thread.currentThread().sleep(3000);


    }


}