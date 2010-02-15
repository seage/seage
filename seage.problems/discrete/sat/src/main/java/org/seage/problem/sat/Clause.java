package org.seage.problem.sat;

/**
 * Summary description for Clause.
 */
public class Clause {

    private Literal[] _literals;
    private int _numberLiterals;
    private Literal[] _copyLiterals;

    public Clause(Literal[] literals) {
        _literals = literals;
        _numberLiterals = literals.length;
    }

    public int Size() {
        return _numberLiterals;
    }

    public Literal[] getLiterals() {
        return _literals;
    }

    public Literal[] copyLiterals() {
        _copyLiterals = _literals.clone();
        return _copyLiterals;
    }

    public void setLiterals(Literal[] literals) {
        _literals = literals;
    }

    public int numberNegLitInClause() {
        int numberNegLiterals = 0;
        for (int i = 0; i < _numberLiterals; i++) {
            if (getLiterals()[i].isNeg()) {
                numberNegLiterals++;
            }
        }
        return numberNegLiterals;
    }

    public boolean trueClause() {
        boolean isTrue = false;
        for (int i = 0; i < _numberLiterals; i++) {
            if (!_literals[i].isNeg()) {
                isTrue = true;
            }
        }
        return isTrue;
    }

    public String toString() {
        String result = "";

        for (int i = 0; i < _literals.length; i++) {
            result += _literals[i].toString() + "\t";
        }
        return result;
    }
}
