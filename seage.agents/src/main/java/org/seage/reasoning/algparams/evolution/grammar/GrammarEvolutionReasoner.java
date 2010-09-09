/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.metaheuristic.genetics.Subject;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.AlgorithmReportEvaluator;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.evolution.EvolutionReasoner;

/**
 * Evolution of grammar rules used for algorithm parameter settings.
 * @author rick
 */
public class GrammarEvolutionReasoner extends EvolutionReasoner
{

    public GrammarEvolutionReasoner(Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
    }

    @Override
    protected Policy createPolicy(Subject subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
