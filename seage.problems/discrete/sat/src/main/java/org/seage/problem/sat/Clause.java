package org.seage.problem.sat;

/**
 * Summary description for Clause.
 */
public class Clause {

    private Literal[] _literals;

    public Clause(Literal[] literals) {
        _literals = literals;
    }

    public Literal[] getLiterals() {
        return _literals;
    }

    public Literal[] copyLiterals() {
        return _literals.clone();
    }

    public void setLiterals(Literal[] literals) {
        _literals = literals;
    }

    public int numberNegLitInClause() {
        int numberNegLiterals = 0;
        for (int i = 0; i < _literals.length; i++) {
            if (getLiterals()[i].isNeg()) {
                numberNegLiterals++;
            }
        }
        return numberNegLiterals;
    }

    public boolean isTrue() {
        boolean isTrue = false;
        for (int i = 0; i < _literals.length; i++) {
            if (!_literals[i].isNeg()) {
                isTrue = true;
            }
        }
        return isTrue;
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < _literals.length; i++) {
            result += _literals[i].toString() + "\t";
        }
        return result;
    }
}
