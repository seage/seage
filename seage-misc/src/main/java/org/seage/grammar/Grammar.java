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

package org.seage.grammar;

import java.util.*;

/**
 *
 * @author jenik
 */
public class Grammar {

    public Grammar(NonterminalSymbol startSymbol) {
        this.startSymbol = startSymbol;
        this.rules = new HashMap<NonterminalSymbol, Vector<GrammarRule> > ();
        this.terminals = new HashSet<TerminalSymbol>();
        this.nonterminals = new HashSet<NonterminalSymbol>();
        this.rSet = new HashSet<GrammarRule>();
        this.terminalRules = new HashMap<NonterminalSymbol, Vector<GrammarRule> >();
        this.maxRecursions = new HashMap<GrammarRule, Integer>();
    }
    
    /** @brief add rule to set */
    public void addRule(GrammarRule rule, int maxRecursionDepth) {
        NonterminalSymbol left = rule.getLeft();            
        Vector<Symbol> right = rule.getRight();
        Vector<GrammarRule> r = rules.get(left);
        if (r == null) {
            r = new Vector<GrammarRule>();            
            rules.put(left, r);            
        }
        r.add(rule);
        //add symbols to sets
        Iterator<Symbol> it = right.iterator();
        while (it.hasNext()) {
            Symbol s = (Symbol)it.next();
            if (s.getType() == Symbol.Type.TERMINAL)
                terminals.add((TerminalSymbol)s);
            else
                nonterminals.add((NonterminalSymbol)s);
        }
        //add rule
        rSet.add(rule);
        maxRecursions.put(rule, maxRecursionDepth);

        System.out.println(rule.toString());
    }
        
    /** @brief retrieve grammar rule accoring to left side 
     * 
     * @param left desired left side
     * @param pos number of the rule identifying it in set 
     * of rules with same left side
     * @param terminating tells that we want rule from set of terminalRules
     * @return grammar rule or throw exception
     */
    GrammarRule getRule(NonterminalSymbol left, int pos, boolean terminating) {
        Vector<GrammarRule> r;
        if (!terminating)
            r = rules.get(left);
        else
            r = terminalRules.get(left);
        return r.get(pos % r.size());
    }
    
    protected Integer getMaxRecursion(GrammarRule rule) {
        return maxRecursions.get(rule);
    }

    /** @brief calculate set "terminating nonterminals"
     * 
     */
    public void calculateTerminateRules() throws Exception {
        /** ALGORITHM: iterate through grammar and test if all 
                                symbols on right side of grammar are terminals or members of
                                "terminatable", if so add the rule to terminating, and symbol on 
                                left side to "terminatable". iterate as long as the set grows */
        Set<NonterminalSymbol> terminatable = new HashSet<NonterminalSymbol>();
        Set<NonterminalSymbol> addTerminatable = new HashSet<NonterminalSymbol>();
        terminalRules.clear();
        boolean added = true;
        while (added) {
            terminatable.addAll(addTerminatable);
            added = false;
            Iterator<GrammarRule> it = rSet.iterator();
            while (it.hasNext()) {
                GrammarRule rule = (GrammarRule)it.next();
                /** skip in order to prevent the cycles */
                if (terminatable.contains(rule.getLeft()))
                    continue;
                Vector<Symbol> right = rule.getRight();
                Iterator<Symbol> rit = right.iterator();
                boolean thisTerminatable = true;
                while (rit.hasNext()) {
                    Symbol s = (Symbol)rit.next();
                    if (s.getType() != Symbol.Type.TERMINAL &&
                            !terminatable.contains((NonterminalSymbol)s) &&
                            !addTerminatable.contains((NonterminalSymbol)s))
                        thisTerminatable = false;
                }
                if (thisTerminatable) {
//                    System.out.println("Adding rule " + rule + " to terminating rules");
                    if (!terminalRules.containsKey(rule.getLeft())) {
                        terminalRules.put(rule.getLeft(), new Vector<GrammarRule> ());
                    }
                    Vector<GrammarRule> v = terminalRules.get(rule.getLeft());
                    v.add(rule);
                    addTerminatable.add(rule.getLeft());
                    added = true;
                }
            }
        }
        //test is all nonterminals are in terminatable set
        if (!terminatable.containsAll(nonterminals))
            throw new Exception("Not all symbols are terminatable!");
    }

    public String toString()
    {
        String result = "";

        return result;
    }
    
    
    Set<TerminalSymbol> getTerminals() { return terminals; }
    Set<NonterminalSymbol> getNonterminals() { return nonterminals; }
    
    protected Set<TerminalSymbol> terminals;
    protected Set<NonterminalSymbol> nonterminals;
    protected HashMap<GrammarRule, Integer> maxRecursions;

    //Key is left side of grammar rule
    protected HashMap<NonterminalSymbol, Vector<GrammarRule> > rules;
    protected HashSet<GrammarRule> rSet;
    protected HashMap<NonterminalSymbol, Vector<GrammarRule> > terminalRules;
    public NonterminalSymbol startSymbol;
    
}
