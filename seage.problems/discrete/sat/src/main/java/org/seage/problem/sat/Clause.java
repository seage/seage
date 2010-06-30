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

    public void setLiterals(Literal[] literals) {
        _literals = literals;
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
