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

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.data.DataNode;
import org.seage.grammar.NonterminalSymbol;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import java.io.Serializable;
import java.util.HashMap;

/**
 * A policy based on grammar rules
 * @author Richard Malek
 */
public class GrammarRulePolicy extends Policy
{
    private int _id;
    private HashMap<String, NonterminalSymbol> _rules;

    public GrammarRulePolicy(Attribute[] attributes, AlgorithmReportEvaluator re, int id, HashMap<String, NonterminalSymbol> rules)
    {
        super(attributes, re);
        _id = id;
        _rules = (HashMap)rules.clone();
    }

    public DataNode suggest(DataNode x) throws Exception
    {
        DataNode result = (DataNode)x.clone();
        for(String name : _rules.keySet())
        {
            result.putValue(name, _rules.get(name).eval(x));
        }

        return result;
    }

    public int getID()
    {
        return _id;
    }

}
