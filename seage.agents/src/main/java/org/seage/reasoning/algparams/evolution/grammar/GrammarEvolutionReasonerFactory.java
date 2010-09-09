/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.data.DataNode;
import ailibrary.grammar.Grammar;
import ailibrary.grammar.iif.IifGrammar;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import java.util.Vector;

/**
 * A factory that creates a GrammarReasoner
 * @author rick
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
