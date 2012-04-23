/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zmatlja1
 */
public class RMProcess {
    
    private String resourceName;    
    private String operatorName;
    private String reportName;
    private List<String> operatorOutputPorts;  

    public RMProcess(String resourceName, String operatorName, String reportName) {
        this.resourceName = resourceName;
        this.operatorName = operatorName;
        this.reportName = reportName;
        operatorOutputPorts = new ArrayList<String>(); 
    }

    public RMProcess(String resourceName, String operatorName, String reportName, List<String> operatorOutputPorts) {
        this.resourceName = resourceName;
        this.operatorName = operatorName;
        this.reportName = reportName;
        this.operatorOutputPorts = operatorOutputPorts;
    }

    public void addOperatorOutputPort(String name)
    {
        this.operatorOutputPorts.add( name );
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<String> getOperatorOutputPorts() {
        return operatorOutputPorts;
    }

    public void setOperatorOutputPorts(List<String> operatorOutputPorts) {
        this.operatorOutputPorts = operatorOutputPorts;
    }

}
