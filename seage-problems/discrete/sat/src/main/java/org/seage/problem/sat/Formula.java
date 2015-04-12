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
package org.seage.problem.sat;

import java.util.*;

import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;

/**
 * Summary description for Formula.
 */
public class Formula extends ProblemInstance 
{

    private ArrayList<Clause> _clauses;
    private int _literalCount;
    private Clause[] __null;
    private ArrayList< ArrayList<Literal>> _literals;

    public Formula(ProblemInstanceInfo instanceInfo, List<Clause> clauses)
	{
    	super(instanceInfo);		

        _clauses = new ArrayList<Clause>(clauses);
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
