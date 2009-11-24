
package org.seage.metaheuristic.sannealing;

/**
 * @author Jan Zmátlík
 */
public interface ISimulatedAnnealing {
        double getMaximalTemperature();
        void setMaximalTemperature(double maximalTemperature);

        double getMinimalTemperature();
        void setMinimalTemperature(double minimalTemperature);

        double getAnnealingCoefficient();
        void setAnnealingCoefficient(double alpha);

        double getSolution();
        void start();
        void stop();
}
