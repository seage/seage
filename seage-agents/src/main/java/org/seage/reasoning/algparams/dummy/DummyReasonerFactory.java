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
package org.seage.reasoning.algparams.dummy;

import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import java.util.Vector;

/**
 * @author Richard Malek
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
