/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.hillclimber;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.seage.metaheuristic.hillclimber.HillClimber;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatHillClimberTest {

    static Date _date;
    static SimpleDateFormat _hours = new SimpleDateFormat("h");
    static SimpleDateFormat _minutes = new SimpleDateFormat("m");
    static SimpleDateFormat _seconds = new SimpleDateFormat("s");
    static SimpleDateFormat _milisec = new SimpleDateFormat("S");
    static int _h, _m, _s;
    static double _actualTime, _ms;

    public static double getTime() {
        _date = new Date();
        _h = Integer.parseInt(_hours.format(_date));
        _m = Integer.parseInt(_minutes.format(_date));
        _s = Integer.parseInt(_seconds.format(_date));
        _ms = Double.parseDouble(_milisec.format(_date));
        _actualTime = _h * 3600 + _m * 60 + _s + _ms / 1000;
        return _actualTime;
    }

    public static void testing1(Formula formula) throws Exception {
        SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
        SatSolutionGenerator solGen;
        HillClimber hc;

        String solutionType = "";
        int[] iterations = {1, 10, 100, 1000};
        int[] restarts = {1, 5, 10, 50, 100};

        int repeats = 10;
        double sumSol, sumTime, hlpTime;
        SatSolution actualBestSol, bestSol = null;

//        solGen = new SatSolutionGenerator("greedy", formula);
//        hc = new HillClimber(objFce, new SatMoveManager(), solGen, iter);
//        hc.startRestartedSearching(classic, 10);
//        SatSolution actualBestSol = (SatSolution) hc.getBestSolution();
//        System.out.println(" false clauses: " + actualBestSol.getObjectiveValue());

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

    public static void testing2(Formula formula) throws Exception {
        SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
        SatMoveManager moveManager = new SatMoveManager();
        SatSolutionGenerator solGen = new SatSolutionGenerator("Greedy", formula);
        
        HillClimber hc;

        int[] iterations = {2000};
        int[] restarts = {1};

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
        String path = "data/uf75/uf75-050.cnf";
        Formula formula = FormulaReader.readFormula(path);
        testing2(formula);
    }
}
