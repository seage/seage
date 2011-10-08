package org.seage.reasoning.algparams.evolution;

import org.seage.aal.reporting.AlgorithmReport;
import org.seage.metaheuristic.genetics.Evaluator;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Genome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.aal.reporting.AlgorithmReportEvaluator;
import org.seage.metaheuristic.genetics.Gene;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Reasoner working with evolution to adapt policies
 * @author rick
 */
public abstract class EvolutionReasoner extends Reasoner
{
    // <editor-fold desc="EvolutionReasoner members">
    protected int NUM_SOLUTION = 8;

    protected ArrayList<Subject> _subjects;
    protected ArrayList<Subject> _subjectsToEval;
    protected HashMap<Integer, Subject> _subjectsInEval;

    private PolicyGeneticOperator _geneticOperator;
    
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public EvolutionReasoner(Policy.Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
        
        _subjects = new ArrayList<Subject>();
        _subjectsToEval = new ArrayList<Subject>();
        _subjectsInEval = new HashMap<Integer, Subject>();

        _geneticOperator = createGeneticOperator();
        
        new Thread(new EvolutionThread()).start();
    }
    // </editor-fold>

    // <editor-fold desc="Reasoner methods">
    @Override
    public synchronized Policy getPolicy() throws Exception
    {
        if(_subjectsToEval.size() == 0)
            _subjectsToEval.add(_geneticOperator.randomize(new Subject(new Genome(1, _attributes.length))));

        Subject ps = _subjectsToEval.remove(0);
        if(ps.getObjectiveValue() != null && ps.getObjectiveValue().length > 0)
            System.out.println(ps.getObjectiveValue()[0]+" - "+ ps.toString() + "**************************");
        
        Policy result= createPolicy(ps);
        _subjectsInEval.put(result.getID(), ps);
        return result;
    }
    @Override
    public synchronized void putPolicyReport(AlgorithmReport report) throws Exception
    {
        if(report.getId() == 0) return;
        Subject s = _subjectsInEval.remove(report.getId());
        double prevFitness = 0;
//        if(s.getObjectiveValue() != null && s.getObjectiveValue().length > 0)
//            prevFitness = s.getObjectiveValue()[0];
        s.setObjectiveValue(new double[]{prevFitness-_runtimeEvaluator.evaluate(report)});
        
        _subjects.add(s);
    }
    // </editor-fold>

    // <editor-fold desc="EvolutionReasoner methods">

    /**
     * Method for overriding purposes.
     * @return PolicyGeneticOperator
     */
    protected PolicyGeneticOperator createGeneticOperator()
    {
        return new PolicyGeneticOperator();
    }

    /**
     *
     * @param subject
     * @return EvolutionPolicy
     */
    protected abstract Policy createPolicy(Subject subject);

    // </editor-fold>

    // <editor-fold desc="Implementation of Genetic Algorithm classes">
    protected class PolicyGeneticOperator extends GeneticOperator
    {

        @Override
        public Subject[] crossOver(Subject parent1, Subject parent2) throws Exception {
            return super.crossOver(parent1, parent2);
        }

        @Override
        public int[] select(Subject[] subjects) {
            return super.select(subjects);
        }


        @Override
        public Subject mutate(Subject subject) throws Exception
        {
            if(_random.nextDouble() < _mutatePct)
            {
                for(int i=0;i<subject.getGenome().getChromosome(0).getLength();i++)
                {
                    if(_random.nextDouble() < _mutateLengthPct)
                    {
                        Gene g = subject.getGenome().getChromosome(0).getGene(i);
                        g.setValue(randomAtt(_attributes[i]));
                    }
                }
            }
            
            return subject;
        }

        @Override
        public Subject randomize(Subject subject) throws Exception
        {
            if(subject.getGenome().getChromosome(0).getLength() != _attributes.length)
                return subject;

            for(int j=0;j<_attributes.length;j++)
            {
                int geneVal = randomAtt(_attributes[j]);
                subject.getGenome().getChromosome(0).setGene(j, new Gene(geneVal));
            }
            return subject;
        }

        private int randomAtt(Policy.Attribute a)
        {
            return (int)(a.MinValue + (Math.random() * (a.MaxValue - a.MinValue)));
        }

    }

    private class SubjectComparator implements Comparator<Subject>
    {

        public int compare(Subject s1, Subject s2) {
            return (int)(s2.getFitness()[0] - s1.getFitness()[0]);
        }

    }

    // </editor-fold>

    private class EvolutionThread implements Runnable
    {

        public void run()
        {
            try
            {
                while(true)
                {
                    if(_subjects.size() > NUM_SOLUTION/2 && _subjectsToEval.size()<NUM_SOLUTION/2)
                    {
                        Collections.sort(_subjects, new SubjectComparator());
                        if(_subjects.size() > NUM_SOLUTION)                        
                            _subjects = new ArrayList<Subject>(_subjects.subList(0, NUM_SOLUTION));
                        
                        int ixs[] = _geneticOperator.select(_subjects.toArray(new Subject[0]));
                        Subject[] children = _geneticOperator.crossOver(_subjects.get(ixs[0]), _subjects.get(ixs[1]));
                        _subjectsToEval.add(_geneticOperator.mutate(children[0]));
                        _subjectsToEval.add(_geneticOperator.mutate(children[1]));

                        System.out.println("****************************Parameter evolution.");
                    }
                    else
                    {
                        Thread.sleep(1000);
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

    }
}
