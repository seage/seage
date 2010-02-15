package org.seage.problem.sat;

import java.util.*;

/**
 * Summary description for Formula.
 */
public class Formula {

    private ArrayList _clauses;
    private int _literalCount;
    private Clause[] __null;


    public Formula(List<Clause> clauses)
    {
        _clauses = new ArrayList(clauses);
        __null = new Clause[0];

        for(Clause c : clauses)
            for(Literal l : c.getLiterals())
                if(l.getIndex() >= _literalCount)
                    _literalCount =l.getIndex()+1;
    }

    //OK
    public Clause[] getClauses() {
        return (Clause[]) _clauses.toArray(__null);
    }

    //OK
    public Clause getClause(int index) {
        return (Clause) _clauses.get(index);
    }

    //OK
    public int getLiteralCount() {
        return _literalCount;
    }

    //OK
    public int negLitInReaded() {
        int negLiterals = 0;
        for (int i = 0; i < _clauses.size(); i++) {
            Clause cl = (Clause) _clauses.get(i);
            negLiterals += cl.numberNegLitInClause();
        }
        return negLiterals;
    }

    //OK
    public int getNumberOfTrueClauses() {
        int trueClauses = 0;
        Clause clause;
        for (int i = 0; i < _clauses.size(); i++) {
            clause = (Clause) _clauses.get(i);
            if (clause.isTrue()) {
                trueClauses++;
            }
        }
        return trueClauses;
    }

    //OK
    public int getNumberOfFalseClauses() {
        int falseClauses = 0;
        Clause clause;
        for (int i = 0; i < _clauses.size(); i++) {
            clause = (Clause) _clauses.get(i);
            if (!clause.isTrue()) {
                falseClauses++;
            }
        }
        return falseClauses;
    }

//    //OK
//    public void substituteLiteralsInFormula(SatSolution sol) {
//        Clause clause;
//        Literal[] literals;
//        Literal[] newLiterals;
//        _substitutedClauses = (ArrayList)_clauses.clone();
//        for (int i = 0; i < sol.getLiterals().length; i++) {
//            if (sol.getLiteral(i).isNeg()) {
//                for (int j = 0; j < _clauses.size(); j++) {
//                    clause = (Clause)_substitutedClauses.get(j);
//                    literals = clause.getLiterals();
//                    newLiterals = literals.clone();
//                    for (int k = 0; k < clause.Size(); k++) {
//                        if (literals[k].getAbsValue() == sol.getLiteral(i).getAbsValue()) {
//                            newLiterals[k] = new Literal(-literals[k].getValue());
//                        }
//                    }
//                    _substitutedClauses.remove(j);
//                    _substitutedClauses.add(j, new Clause(newLiterals));
//                }
//            }
//        }
//    }

    //OK
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < _clauses.size(); i++) {
            result += _clauses.get(i).toString() + "\n";
        }
        return result;
    }

    //OK
    public void printFormula(){
        String result = "";
        for (int i = 0; i < _clauses.size(); i++) {
            result += _clauses.get(i).toString() + "\n";
        }
        System.out.println(""+result);
    }

//    //OK
//    public void printSubstitutedFormula(){
//        String result = "";
//        for (int i = 0; i < _substitutedClauses.size(); i++) {
//            result += _substitutedClauses.get(i).toString() + "\n";
//        }
//        System.out.println(""+result);
//    }
}
