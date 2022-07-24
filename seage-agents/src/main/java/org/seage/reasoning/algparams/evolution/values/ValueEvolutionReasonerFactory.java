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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.reasoning.algparams.evolution.values;

import org.seage.aal.reporter.AlgorithmReportEvaluator;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import java.util.Vector;

/**
 *
 * @author Richard Malek
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
