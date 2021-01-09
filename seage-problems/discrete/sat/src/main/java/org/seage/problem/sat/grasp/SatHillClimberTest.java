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
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat.grasp;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.grasp.HillClimber;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatHillClimberTest {

  private Date _date;
  private SimpleDateFormat _hours = new SimpleDateFormat("h");
  private SimpleDateFormat _minutes = new SimpleDateFormat("m");
  private SimpleDateFormat _seconds = new SimpleDateFormat("s");
  private SimpleDateFormat _milisec = new SimpleDateFormat("S");
  private int _h, _m, _s;
  private double _actualTime, _ms;

  public double getTime() {
    _date = new Date();
    _h = Integer.parseInt(_hours.format(_date));
    _m = Integer.parseInt(_minutes.format(_date));
    _s = Integer.parseInt(_seconds.format(_date));
    _ms = Double.parseDouble(_milisec.format(_date));
    _actualTime = _h * 3600 + _m * 60 + _s + _ms / 1000;
    return _actualTime;
  }

  public void testing1(Formula formula) throws Exception {
    SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
    SatSolutionGenerator solGen;
    HillClimber hc;

    String solutionType = "";
    int[] iterations = { 1, 10, 100, 1000 };
    int[] restarts = { 1, 5, 10, 50, 100 };

    int repeats = 10;
    double sumSol, sumTime, hlpTime;
    SatSolution actualBestSol, bestSol = null;

    // solGen = new SatSolutionGenerator("greedy", formula);
    // hc = new HillClimber(objFce, new SatMoveManager(), solGen, iter);
    // hc.startRestartedSearching(classic, 10);
    // SatSolution actualBestSol = (SatSolution) hc.getBestSolution();
    // System.out.println(" false clauses: " +
    // actualBestSol.getObjectiveValue());

    for (int i = 1; i <= 2; i++) {
      switch (i) {
      case 1:
        solutionType = "random";
        break;
      case 2:
        solutionType = "greedy";
        break;
      }
      solGen = new SatSolutionGenerator(solutionType, formula);
      for (int res : restarts) {
        for (int iter : iterations) {
          sumSol = 0;
          sumTime = 0;
          for (int j = 1; j <= repeats; j++) {
            hc = new HillClimber(objFce, new SatMoveManager(), solGen, iter);
            hlpTime = getTime();
            hc.startRestartedSearching(res);
            sumTime = getTime() - hlpTime;
            actualBestSol = (SatSolution) hc.getBestSolution();
            if (j > 1) {
              if (actualBestSol.getObjectiveValue() < bestSol.getObjectiveValue()) {
                bestSol = actualBestSol;
              }
            } else {
              bestSol = actualBestSol;
            }
            sumSol += actualBestSol.getObjectiveValue();
          }
          System.out.print(solutionType);
          System.out.print("\t" + res + "\t" + iter);
          System.out.print("\t" + (sumSol / repeats));
          System.out.print("\t" + bestSol.getObjectiveValue());
          System.out.println("\t" + (sumTime / repeats));
        }
      }
    }
  }

  public void testing2(Formula formula) throws Exception {
    SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
    SatMoveManager moveManager = new SatMoveManager();
    SatSolutionGenerator solGen = new SatSolutionGenerator("Greedy", formula);

    HillClimber hc;

    int[] iterations = { 2000 };
    int[] restarts = { 5000 };

    double sumTime, hlpTime;

    for (int res : restarts) {
      for (int iter : iterations) {
        hc = new HillClimber(objFce, moveManager, solGen, iter);
        hlpTime = getTime();
        hc.startRestartedSearching(res);
        sumTime = getTime() - hlpTime;
        System.out.print("\t" + res);
        System.out.print("\t" + hc.getBestSolution().getObjectiveValue());
        System.out.println("\t" + sumTime);
      }
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    String path = "data/sat/uf100-01.cnf";

    SatHillClimberTest test = new SatHillClimberTest();
    Formula formula = new Formula(new ProblemInstanceInfo("uf20", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(new FileInputStream(path)));

    test.testing2(formula);
  }
}
