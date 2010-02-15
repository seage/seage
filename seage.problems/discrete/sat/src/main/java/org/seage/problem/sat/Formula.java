package org.seage.problem.sat;

import java.util.*;
import org.seage.problem.sat.hillclimber.SatSolution;

/**
 * Summary description for Formula.
 */
public class Formula {

    private ArrayList _readedClauses;
    private ArrayList _substitutedClauses;
    private Clause[] __null;

    //OK
    public Formula() {
        _readedClauses = new ArrayList();
        _substitutedClauses = new ArrayList();
        __null = new Clause[0];
    }

    //OK
    public void readClause(Clause clause) {
        _readedClauses.add(clause);
    }

    //OK
    public void readArrayClauses(Clause[] clauses) {
        for (int i = 0; i < clauses.length; i++) {
            readClause(clauses[i]);
        }
    }

    //OK
    public Clause[] getReadedClauses() {
        return (Clause[]) _readedClauses.toArray(__null);
    }

    //OK
    public Clause getReadedClause(int index) {
        return (Clause) _readedClauses.get(index);
    }

    //OK
    public int getNumLiteral() {
        int max = 0;
        for (int i = 0; i < _readedClauses.size(); i++) {
            Clause cl = (Clause) _readedClauses.get(i);
            Literal[] lit = cl.getLiterals();
            for (int j = 0; j < lit.length; j++) {
                if (lit[j].getAbsValue() > max) {
                    max = lit[j].getAbsValue();
                }
            }
        }
        return max;
    }

    //OK
    public int negLitInReaded() {
        int negLiterals = 0;
        for (int i = 0; i < _readedClauses.size(); i++) {
            Clause cl = (Clause) _readedClauses.get(i);
            negLiterals += cl.numberNegLitInClause();
        }
        return negLiterals;
    }

    //OK
    public int numberTrueClausesAfterSubstitute() {
        int trueClauses = 0;
        Clause clause;
        for (int i = 0; i < _substitutedClauses.size(); i++) {
            clause = (Clause) _substitutedClauses.get(i);
            if (clause.trueClause()) {
                trueClauses++;
            }
        }
        return trueClauses;
    }

    //OK
    public int numberTrueClausesInReadedFormula() {
        int trueClauses = 0;
        Clause clause;
        for (int i = 0; i < _readedClauses.size(); i++) {
            clause = (Clause) _readedClauses.get(i);
            if (clause.trueClause()) {
                trueClauses++;
            }
        }
        return trueClauses;
    }

    //OK
    public int numberFalseClausesAfterSubstitute() {
        int falseClauses = 0;
        Clause clause;
        for (int i = 0; i < _substitutedClauses.size(); i++) {
            clause = (Clause) _substitutedClauses.get(i);
            if (!clause.trueClause()) {
                falseClauses++;
            }
        }
        return falseClauses;
    }

    //OK
    public int numberFalseClausesInReadedFormula() {
        int falseClauses = 0;
        Clause clause;
        for (int i = 0; i < _readedClauses.size(); i++) {
            clause = (Clause) _readedClauses.get(i);
            if (!clause.trueClause()) {
                falseClauses++;
            }
        }
        return falseClauses;
    }

    //OK
    public void substituteLiteralsInFormula(SatSolution sol) {
        Clause clause;
        Literal[] literals;
        Literal[] newLiterals;
        _substitutedClauses = (ArrayList)_readedClauses.clone();
        for (int i = 0; i < sol.getLiterals().length; i++) {
            if (sol.getLiteral(i).isNeg()) {
                for (int j = 0; j < _readedClauses.size(); j++) {
                    clause = (Clause)_substitutedClauses.get(j);
                    literals = clause.getLiterals();
                    newLiterals = literals.clone();
                    for (int k = 0; k < clause.Size(); k++) {
                        if (literals[k].getAbsValue() == sol.getLiteral(i).getAbsValue()) {
                            newLiterals[k] = new Literal(-literals[k].getLiteral());
                        }
                    }
                    _substitutedClauses.remove(j);
                    _substitutedClauses.add(j, new Clause(newLiterals));
                }
            }
        }
    }

    //OK
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < _readedClauses.size(); i++) {
            result += _readedClauses.get(i).toString() + "\n";
        }
        return result;
    }

    //OK
    public void printReadedFormula(){
        String result = "";
        for (int i = 0; i < _readedClauses.size(); i++) {
            result += _readedClauses.get(i).toString() + "\n";
        }
        System.out.println(""+result);
    }

    //OK
    public void printSubstitutedFormula(){
        String result = "";
        for (int i = 0; i < _substitutedClauses.size(); i++) {
            result += _substitutedClauses.get(i).toString() + "\n";
        }
        System.out.println(""+result);
    }
}
