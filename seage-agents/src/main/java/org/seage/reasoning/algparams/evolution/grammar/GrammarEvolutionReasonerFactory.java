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

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.data.DataNode;
import org.seage.grammar.Grammar;
import org.seage.grammar.iif.IifGrammar;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import java.util.Vector;

/**
 * A factory that creates a GrammarReasoner
 * @author Richard Malek
 */
public class GrammarEvolutionReasonerFactory implements  ReasonerFactory
{
    @Override
    public Reasoner createReasoner(DataNode param) throws Exception 
    {
        Vector<String> names = new Vector<String>();
        Vector<Attribute> atts = new Vector<Attribute>();
        for(DataNode dn : param.getDataNodes("attr"))
        {
            names.add(dn.getValueStr("name"));
            atts.add(new Attribute(dn.getValueStr("name"), dn.getValueDouble("minValue"), dn.getValueDouble("maxValue")));
        }

        Grammar grammar = new IifGrammar(names);
//        GrammarPolicySubject[] subjects = new GrammarPolicySubject[10];
//        for(int i=0;i<10;i++) subjects[i] = new GrammarPolicySubject(atts.toArray(new Attribute[0]), grammar, new Genome(1, 10));

        return new GrammarEvolutionReasoner(atts.toArray(new Attribute[]{}), null);
    }

}
