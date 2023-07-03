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
import java.util.List;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Summary description for Formula.
 */
public class Formula extends ProblemInstance {
  private static final Logger log = LoggerFactory.getLogger(Formula.class.getName());

  private ArrayList<Clause> clauses;
  private int literalCount;

  /**
   * .
   */
  public Formula(ProblemInstanceInfo instanceInfo, List<Clause> clauses) {
    super(instanceInfo);

    this.clauses = new ArrayList<>(clauses);
    this.literalCount = 0;
    
    for (Clause c : clauses) {
      for (Literal l : c.getLiterals()) {
        if (l.getId() > literalCount) {
          literalCount = l.getId();
        }
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
    log.debug(toString());
  }

}
