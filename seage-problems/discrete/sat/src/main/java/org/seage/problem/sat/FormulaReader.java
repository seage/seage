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
import java.io.*;

/**
 * Summary description for InstanceReader.
 */
public class FormulaReader
{

    public static Formula readFormula(String path) throws Exception
    {
        return new Formula(readClauses(path));
    }

    private static List<Clause>  readClauses(String path) throws IOException
    {
        LineNumberReader lnr = new LineNumberReader(new FileReader(path));
        lnr.setLineNumber(1);
        StreamTokenizer stok = new StreamTokenizer(lnr);

        int[] dataLine = null;
        ArrayList<Clause> clauses = new ArrayList<Clause>();


        while ((dataLine = readLine(stok)) != null)
        {
            if (dataLine.length < 3)
            {
                continue;
            }
            Literal[] literals = new Literal[3];
            for (int i = 0; i < 3; i++)
            {
                boolean neg = dataLine[i] < 0? true:false;
                int ix = Math.abs(dataLine[i])-1;

                literals[i] = new Literal(ix, neg);				/////////////////////
            }

            Clause newClause = new Clause(literals);

            clauses.add(newClause);
        }

        return clauses;
    }

    private static int[] readLine(StreamTokenizer stok) throws IOException
    {
        ArrayList<Integer> res = new ArrayList<Integer>();
        double sum = 0;
        int[] result = null;
        boolean comment = false;
        stok.parseNumbers();
        stok.eolIsSignificant(true);
        stok.commentChar(35);

        //if(stok.ttype == StreamTokenizer.TT_EOF) return null;
        while (sum == 0)
        {
            stok.nextToken();

            while (stok.ttype != StreamTokenizer.TT_EOL && stok.ttype != StreamTokenizer.TT_EOF)
            {
                if (stok.ttype == StreamTokenizer.TT_NUMBER)
                {
                    res.add(new Integer((int) stok.nval));
                    sum++;//= stok.nval;
                }
                if (stok.ttype == StreamTokenizer.TT_WORD)
                {
                    comment = true;
                    break;
                }

                stok.nextToken();
            }
            if (stok.ttype == StreamTokenizer.TT_EOF)
            {
                if (sum != 0)
                {
                    break;
                }
                else
                {
                    return null;
                }
            }
        }
        if (comment)        
            return new int[]{};
        
        result = new int[res.size()];

        for (int i = 0; i < result.length; i++)        
            result[i] = ((Integer) res.get(i)).intValue();        

        return result;
    }
}
