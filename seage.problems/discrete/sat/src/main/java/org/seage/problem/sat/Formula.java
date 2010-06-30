package org.seage.problem.sat;

import java.util.*;

/**
 * Summary description for Formula.
 */
public class Formula {

    private ArrayList _clauses;
    private int _literalCount;
    private Clause[] __null;
    private ArrayList< ArrayList<Literal>> _literals;

    public Formula(List<Clause> clauses)
    {
        _clauses = new ArrayList(clauses);
        __null = new Clause[0];

        for(Clause c : clauses)
            for(Literal l : c.getLiterals())
                if(l.getIndex() >= _literalCount)
                    _literalCount =l.getIndex()+1;

        _literals = new ArrayList<ArrayList<Literal>>();
        for(int i=0;i<_literalCount;i++) _literals.add(new ArrayList<Literal>());

        for(Clause c : clauses)
            for(Literal l : c.getLiterals())
                _literals.get(l.getIndex()).add(l);
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

    public ArrayList< ArrayList<Literal>> getLiterals()
    {
        return _literals;
    }

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
        System.out.println(toString());
    }

}
