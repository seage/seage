/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest {

    static Date _date;
    static SimpleDateFormat _hours = new SimpleDateFormat("h");
    static SimpleDateFormat _minutes = new SimpleDateFormat("m");
    static SimpleDateFormat _seconds = new SimpleDateFormat("s");
    static SimpleDateFormat _milisec = new SimpleDateFormat("S");
    static int _h, _m, _s;
    static double _actualTime, _ms;

    /**
     * Finding current time
     * @return - Time in seconds
     */
    public static double getTime() {
        _date = new Date();
        _h = Integer.parseInt(_hours.format(_date));
        _m = Integer.parseInt(_minutes.format(_date));
        _s = Integer.parseInt(_seconds.format(_date));
        _ms = Double.parseDouble(_milisec.format(_date));
        _actualTime = _h * 3600 + _m * 60 + _s + _ms / 1000;
        return _actualTime;
    }

    /**
     * First part of testing
     * @param formula - Readed formula
     * @throws Exception
     */
    public static void testing1(Formula formula) throws Exception {
        int numAnts = 100;
        int iterations = 500;
        SatGraph graph;
        SatAntBrain brain;
        SatAntCreator antCreator;
        AntColony colony;

        double sumTime, hlpTime;

        double[] alpha = {1};
        double[] beta = {1, 2, 3};
        double[] defaultPheromone = {0.1, 1};
        double[] quantumPheromone = {0.2, 0.5, 1};
        double[] evaporation = {0.2, 0.5};

        for (double a : alpha) {
            for (double b : beta) {
                brain = new SatAntBrain(a, b, formula);
                for (double def : defaultPheromone) {
                    for (double quant : quantumPheromone) {
                        for (double evapor : evaporation) {
                            graph = new SatGraph(formula, evapor, def);
                            antCreator = new SatAntCreator(graph, brain, numAnts, quant);
                            colony = new AntColony(antCreator, iterations);
                            hlpTime = getTime();
                            colony.beginExploring();
                            sumTime = getTime() - hlpTime;
                            System.out.print(a);
                            System.out.print("\t" + b);
                            System.out.print("\t" + def);
                            System.out.print("\t" + quant);
                            System.out.print("\t" + evapor);
                            System.out.print("\t" + (colony.getGlobalBest() - 0.1));
                            System.out.println("\t" + sumTime);
                        }
                    }
                }
            }
        }
    }

    /**
     * Second part of testing
     * @param formula - readed formula
     * @throws Exception
     */
    public static void testing2(Formula formula) throws Exception {

        SatGraph graph;
        SatAntBrain brain;
        SatAntCreator antCreator;
        AntColony colony;

        double sumTime, hlpTime;

        int numAnts[] = {1};
        int iterations[] = {500};

        double alpha = 1;
        double beta = 1;
        double defaultPheromone = 1;
        double quantumPheromone = 0.2;
        double evaporation = 0.2;

        for (int num : numAnts) {
            for (int iter : iterations) {
                graph = new SatGraph(formula, evaporation, defaultPheromone);
                brain = new SatAntBrain(alpha, beta, formula);
                antCreator = new SatAntCreator(graph, brain, num, quantumPheromone);
                colony = new AntColony(antCreator, iter);
                hlpTime = getTime();
                colony.beginExploring();
                sumTime = getTime() - hlpTime;
                System.out.print("\t" + num);
//                System.out.print("\t" + iter);
                System.out.print("\t" + (colony.getGlobalBest() - 0.1));
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

//        testing2(formula);
//
        double quantumPheromone = 1, evaporation = 0.95, defaultPheromone = 0.1;
        double alpha = 1, beta = 1;
        int numAnts = 100, iterations = 5000;

        SatGraph graph = new SatGraph(formula, evaporation, defaultPheromone);
        SatAntBrain brain = new SatAntBrain(alpha, beta, formula);

        SatAntCreator antCreator = new SatAntCreator(graph, brain, numAnts, quantumPheromone);
        AntColony colony = new AntColony(antCreator, iterations);
        colony.beginExploring();
        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));
        
        boolean[] s = new boolean[colony.getBestPath().size()];
        for(int i=0;i<s.length;i++)
        {
            s[i] = colony.getBestPath().get(i).getNode1().getId() > 0;
            int s2 = 0;
            if(s[i]) s2 =1;
            System.out.print(s2);
        }
        System.out.println();
        System.out.println("Global best: "+ FormulaEvaluator.evaluate(formula, s));
        //graph.printPheromone();
    }
}
