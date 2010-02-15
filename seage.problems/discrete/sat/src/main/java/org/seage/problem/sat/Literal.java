package org.seage.problem.sat;

/**
 * Summary description for Literal.
 */
public class Literal implements java.lang.Cloneable {

    int _literal;

    public Literal(int value) {
        _literal = value;
    }

    public boolean isNeg() {
        return _literal < 0 ? true : false;
    }

    public void neg() {
        _literal = -_literal;
    }

    public void setLiteral(int literal) {
        _literal = literal;
    }

    public int getLiteral() {
        return _literal;
    }

    public int getAbsValue() {
        return Math.abs(_literal);
    }

    @Override
    public String toString() {
        String result = "";
        result += _literal;
        return result;
    }
}
