
package org.seage.metaheuristic.sannealing;

/**
 * @author Jan Zmátlík
 */
public interface ISimulatedAnnealing {
        double getMaximalTemperature();
        double getMinimalTemperature();
        double getAnnealingCoefficient();
        void setMaximalTemperature(double temp);
        void setMinimalTemperature(double temp);
        void setAnnealingCoefficient(double alpha);
        
        double getSolution();
        void start();
        void stop();

}
