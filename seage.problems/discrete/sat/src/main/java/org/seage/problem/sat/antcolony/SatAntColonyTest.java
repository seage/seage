/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.problem.sat.Formula;
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
        int numAnts = 100;
        int iterations = 500;
        SatGraph graph;
        SatAntBrain brain;
        SatAntCreator antCreator;
        AntColony colony;

        double sumTime, hlpTime;

        double[] alpha = {1, 2};
        double[] beta = {1, 2};
        double[] defaultPheromone = {0.1, 1};
        double[] quantumPheromone = {0.01, 0.1, 1};
        double[] evaporation = {0.005, 0.05, 0.5};

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

    public static void testing2(Formula formula) throws Exception {

        SatGraph graph;
        SatAntBrain brain;
        SatAntCreator antCreator;
        AntColony colony;

        double sumTime, hlpTime;

        int numAnts[] = {40000};
        int iterations[] = {600};

        double alpha = 1;
        double beta = 1;
        double defaultPheromone = 0.5;
        double quantumPheromone = 0.5;
        double evaporation = 0.25;

        for (int num : numAnts) {
            for (int iter : iterations) {
                graph = new SatGraph(formula, evaporation, defaultPheromone);
                brain = new SatAntBrain(alpha, beta, formula);
                antCreator = new SatAntCreator(graph, brain, num, quantumPheromone);
                colony = new AntColony(antCreator, iter);
                hlpTime = getTime();
                colony.beginExploring();
                sumTime = getTime() - hlpTime;
                System.out.print("\t" + iter);
                System.out.print("\t" + (colony.getGlobalBest() - 0.1));
                System.out.println("\t" + sumTime);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path = "data/uf100/uf100-0100.cnf";
        Formula formula = FormulaReader.readFormula(path);

        testing2(formula);

//        double quantumPheromone = 0.1, evaporation = 0.5, defaultPheromone = 0.1;
//        double alpha = 1, beta = 2;
//        int numAnts = 100, iterations = 500;

//        SatGraph graph = new SatGraph(formula, evaporation, defaultPheromone);
//        SatAntBrain brain = new SatAntBrain(alpha, beta, formula);
//
//        SatAntCreator antCreator = new SatAntCreator(graph, brain, numAnts, quantumPheromone);
//        AntColony colony = new AntColony(antCreator, iterations);
//        colony.beginExploring();
//        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));
//        graph.printPheromone();
    }
}
