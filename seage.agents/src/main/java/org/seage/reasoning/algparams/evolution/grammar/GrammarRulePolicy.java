/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.data.DataNode;
import ailibrary.grammar.NonterminalSymbol;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import java.io.Serializable;
import java.util.HashMap;

/**
 * A policy based on grammar rules
 * @author rick
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
