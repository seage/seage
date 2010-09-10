/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.evolution.values;

import org.seage.metaheuristic.genetics.Subject;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.AlgorithmReportEvaluator;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.evolution.EvolutionReasoner;
import java.util.HashMap;

/**
 *
 * @author rick
 */
public class ValueEvolutionReasoner extends EvolutionReasoner
{

    public ValueEvolutionReasoner(Attribute[] atts, AlgorithmReportEvaluator re) {
        super(atts, re);
    }

    @Override
    protected Policy createPolicy(Subject subject)
    {
        HashMap<Attribute, Double> values = new HashMap<Attribute, Double>();
        int ix = 0;

        for(Integer i: subject.getGenome().getChromosome(0).getGeneArray())
            values.put(_attributes[ix++], i.doubleValue());
        

        return new ValueDummyPolicy(/*values, _runtimeEvaluator*/);
    }


}
