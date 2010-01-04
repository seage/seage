package ailibrary.grammar;

import java.util.*;
/**
 *
 * @author jenik
 */
public class LeftParse {
    
    public static int MAX_LEN = 11;//503;
    public static int MIN_LEN = 11;//503;
    public static int CONSTANT_LEN = 503; //prime
    public static int CONSTANT_MAX_VAL = 30;
    public static int MAX_EXPANSION_COUNT = 1000;
    
    public LeftParse() {
        _ruleNums = new Vector<Integer>();
        _constantVector = new Vector<Integer>();
    }
        
    /** @brief generate random left parse */
    public void randomGenerate() {
        _ruleNums.clear();
        _constantVector.clear();
        int len = (int)(Math.random()*(MAX_LEN - MIN_LEN)+MIN_LEN);
        for (int i = 0;i < len;i++) {
            _ruleNums.add((int)(Math.random()*255));
        }
        len = CONSTANT_LEN;
        for (int i = 0;i < len;i++) {
            _constantVector.add((int)(Math.random()*CONSTANT_MAX_VAL));
        }

        //178, 135, 229, 37, 116, 246, 44, 15, 98, 158, 211
//        _ruleNums.clear();
//        _ruleNums.add( 178);
//        _ruleNums.add( 135);
//        _ruleNums.add( 229);
//        _ruleNums.add( 37);
//        _ruleNums.add( 116);
//        _ruleNums.add( 246);
//        _ruleNums.add( 44);
//        _ruleNums.add( 15);
//        _ruleNums.add( 98);
//        _ruleNums.add( 158);
//        _ruleNums.add( 211);
    }
    
    /** @brief genetic operator */
    public LeftParse cross(LeftParse other) {
        LeftParse result = new LeftParse();
        result._ruleNums = getCross(this._ruleNums, other._ruleNums);
        result._constantVector = getCross(this._constantVector, other._constantVector);
        return result;
    }
    
    /** @brief mutates this left parse */
    public void mutate() {
        ///mutate rules
        int where = (int)Math.random()*(_ruleNums.size() - 1);
        _ruleNums.set(where, (int)Math.random()*255);
        where = (int)Math.random()*(_constantVector.size() - 1);
        _constantVector.set(where, (int)Math.random()*255);        
    }
        
    public String toString() {
        Iterator it = _ruleNums.iterator();
        String ret = new String();
        boolean comma = false;
        while (it.hasNext()) {
            if (comma)
                ret += ", ";            
            ret += it.next();
            comma = true;
        }
        return ret;
    }
    
    /** @brief create left parse tree according to ruleNums */
    public NonterminalSymbol createParseTree(Grammar gr) throws Exception {
        //first initialize constant generators of terminal symbols        
        for(TerminalSymbol ts : gr.getTerminals())
        {
            Functor f = ts.getGenerator();
            if(f != null)
                f.setVector(_constantVector);
        }
        
        NonterminalSymbol startSymbol = new NonterminalSymbol(gr.startSymbol);
        Stack<NonterminalSymbol> stack = new Stack<NonterminalSymbol>();
        stack.push(startSymbol);
        int i = 0;
        int expansionCount = 0;
        ///NOTE: we must count used grammar rules at given steps to prevent cycles
        Vector<Set<GrammarRule> > used = new Vector<Set<GrammarRule> >();
        ///NOTE: calculate recursions
        HashMap<GrammarRule, Integer> recursions = new HashMap<GrammarRule, Integer> ();
        
        /* NOTE: how many items may be on stack to reset recursion count for given rule set*/
        HashMap<Integer, Set<GrammarRule> > resetSizes =  new HashMap<Integer, Set<GrammarRule> >();                                    
        HashSet<GrammarRule> lockedRecursions = new HashSet<GrammarRule>();
        boolean terminate = false;
        while (!stack.empty()) {
            //test if it isn't time to release rule
            Set<GrammarRule> reset = resetSizes.get(stack.size());
            if (reset != null) {
                for( GrammarRule resetRule : reset)
                {
                    recursions.remove(resetRule);
                    lockedRecursions.remove(resetRule);
                }
                resetSizes.remove(stack.size());                
            }
            Symbol s = stack.pop(); //get symbol
            ///non terminal symbol => do expansion
            NonterminalSymbol nonterm = (NonterminalSymbol)s;
            GrammarRule rule = gr.getRule(nonterm, _ruleNums.get(i), terminate);
            //test if it isn't time to lock rule
            Set<Map.Entry<GrammarRule, Integer> > smap = recursions.entrySet();
            for(Map.Entry<GrammarRule, Integer> me : smap)
            {
                if (me.getValue() == 0) {
                    lockedRecursions.add(me.getKey());
                }
                else if (me.getValue() > 0) {
                    me.setValue(me.getValue() - 1);
                }
            }
            if (lockedRecursions.size() != 0) {
                terminate = true;
            }
            else {
                terminate = false;
            }
            if (++expansionCount >= MAX_EXPANSION_COUNT)
                terminate = true;       //use rules that terminate the parse            
            int tries = 0;            
            while (tries < _ruleNums.size()) {
                //compare used rules in this step
                for (;used.size() <= i;) 
                    used.add(new HashSet<GrammarRule>());
                if (used.get(i).contains(rule)) {
                    i++;        //skip this num
                    tries++;
                    if (i == _ruleNums.size())
                        i = 0;
                    rule = gr.getRule(nonterm, _ruleNums.get(i), terminate);
                }
                else
                    break;
            }
            Integer rec = recursions.get(rule);
            if (rec == null) {
                recursions.put(rule, gr.getMaxRecursion(rule));
                //set current count on stack to release recursion (used grammar rule is done by that count)
                Set<GrammarRule> ruleSet = resetSizes.get(stack.size());
                if (ruleSet == null) {
                    ruleSet = new HashSet<GrammarRule>();
                    resetSizes.put(stack.size(), ruleSet);
                }
                ruleSet.add(rule);                
            }
            used.get(i).add(rule);
            nonterm.expand(stack, rule);
        }
        return startSymbol;
    }
    
    /** @brief cross two vectorsof Integers */
    protected Vector<Integer> getCross(final Vector<Integer> one, final Vector<Integer> other) {
        Vector<Integer> first = null, second = null;
        if (Math.random() > 0.5) {
            first = one;
            second = other;
        }
        else {
            first = other;
            second = one;
        }
        int where = (int)(Math.random()*(first.size() - 2)+1);
        Vector<Integer> result = new Vector<Integer>();
        System.out.println("Crossing first len " + first.size() + " second len " + second.size() + " at " + where);
        for (int i = 0;i < where;i++) {
            result.add(first.get(i));
        }
        for (;where < second.size();where++)
            result.add(second.get(where));
        return result;
    }
    
    protected Vector<Integer> _ruleNums;
    protected Vector<Integer> _constantVector;
        
}
