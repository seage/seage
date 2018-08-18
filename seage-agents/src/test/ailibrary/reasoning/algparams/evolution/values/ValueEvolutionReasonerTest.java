/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package ailibrary.reasoning.algparams.evolution.values;

import org.seage.reasoning.algparams.evolution.values.ValueEvolutionReasonerFactory;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import org.junit.jupiter.api.Test;
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
