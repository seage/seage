package org.seage.problem.sat;

/**
 * Summary description for Literal.
 */
public class Literal implements java.lang.Cloneable {

    private int _index;
    private boolean _neg;

    public Literal(int index, boolean neg) {
        _index = index;
        _neg = neg;
    }

    public boolean isNeg() {
        return _neg;
    }


    public int getIndex()
    {
        return _index;
    }

    @Override
    public String toString() {
        String result = "";
        result += _neg==true?"-":""+_index;
        return result;
    }
}
