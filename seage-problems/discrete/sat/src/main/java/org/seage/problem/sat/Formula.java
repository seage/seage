/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.problem.sat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;

/**
 * Summary description for Formula.
 */
public class Formula extends ProblemInstance {

  private ArrayList<Clause> clauses;
  private int literalCount;

  private HashMap<Integer, Integer> literalsImpact;

  /**
   * .
   */
  public Formula(ProblemInstanceInfo instanceInfo, List<Clause> clauses) {
    super(instanceInfo);

    this.clauses = new ArrayList<>(clauses);

    for (Clause c : clauses) {
      for (Literal l : c.getLiterals()) {
        if (l.getId() > literalCount) {
          literalCount = l.getId();
        }
      }
    }

    this.literalsImpact = new HashMap<>();

    for (Clause c : clauses) {
      for (Literal l : c.getLiterals()) {
        int count = 0;
        int id = l.isNeg() ? -l.getId() : l.getId();
        if (this.literalsImpact.containsKey(id)) {
          count = this.literalsImpact.get(id);
        }
        count++;
        this.literalsImpact.put(id, count);
      }
    }
  }

  // OK
  public List<Clause> getClauses() {
    return clauses;
  }

  // OK
  public Clause getClause(int index) {
    return clauses.get(index);
  }

  // OK
  public int getLiteralCount() {
    return literalCount;
  }

  public Map<Integer, Integer> getLiteralsImpact() {
    return literalsImpact;
  }

  @Override
  public int getSize() {
    return getLiteralCount();
  }

  // OK
  @Override
  public String toString() {
    String result = "";
    for (int i = 0; i < clauses.size(); i++) {
      result += clauses.get(i).toString() + "&";
    }
    return result + "---";
  }

  // OK
  public void printFormula() {
    System.out.println(toString());
  }

}
