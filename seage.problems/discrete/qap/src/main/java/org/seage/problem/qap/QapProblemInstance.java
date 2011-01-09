/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.qap;

import org.seage.aal.ProblemInstance;

/**
 *
 * @author rick
 */
public class QapProblemInstance extends ProblemInstance{
    private Double[][][] _facilityLocation;

    public QapProblemInstance(Double[][][] facilityLocation) {
        _facilityLocation = facilityLocation;
    }

    public Double[][][] getFacilityLocation() {
        return _facilityLocation;
    }
    
    
}
