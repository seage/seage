/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.problem.sat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Summary description for InstanceReader.
 */
public class FormulaReader {

  public static List<Clause> readClauses(InputStream stream) throws IOException {
    LineNumberReader lnr = new LineNumberReader(new BufferedReader(new InputStreamReader(stream)));
    lnr.setLineNumber(1);
    StreamTokenizer stok = new StreamTokenizer(lnr);

    int[] dataLine = null;
    ArrayList<Clause> clauses = new ArrayList<Clause>();

    while ((dataLine = readLine(stok)) != null) {
      // System.out.println(dataLine[0]);
      if (dataLine.length == 0 || dataLine[0] == 0)
        continue;
      List<Literal> literals = new ArrayList<Literal>();
      for (int i = 0; i < dataLine.length; i++) {
        int literalValue = dataLine[i];
        if (literalValue == 0)
          break;
        boolean neg = literalValue < 0 ? true : false;
        int ix = Math.abs(literalValue) - 1;

        literals.add(new Literal(ix, neg)); /////////////////////
      }
      Clause newClause = new Clause(literals.toArray(new Literal[0]));

      clauses.add(newClause);
    }

    return clauses;
  }

  private static int[] readLine(StreamTokenizer stok) throws IOException {
    ArrayList<Integer> res = new ArrayList<Integer>();
    double sum = 0;
    int[] result = null;
    boolean comment = false;
    stok.parseNumbers();
    stok.eolIsSignificant(true);
    stok.commentChar(35);

    // if(stok.ttype == StreamTokenizer.TT_EOF) return null;
    while (sum == 0) {
      stok.nextToken();

      while (stok.ttype != StreamTokenizer.TT_EOL && stok.ttype != StreamTokenizer.TT_EOF) {
        if (stok.ttype == StreamTokenizer.TT_NUMBER) {
          res.add(new Integer((int) stok.nval));
          sum++;// = stok.nval;
        }
        if (stok.ttype == StreamTokenizer.TT_WORD) {
          comment = true;
          break;
        }

        stok.nextToken();
      }
      if (stok.ttype == StreamTokenizer.TT_EOF) {
        if (sum != 0) {
          break;
        } else {
          return null;
        }
      }
    }
    if (comment)
      return new int[] {};

    result = new int[res.size()];

    for (int i = 0; i < result.length; i++)
      result[i] = res.get(i).intValue();

    return result;
  }
}
