/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter._obsolete;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zmatlja1
 */
public class RMProcess
{

    private String resourceName;
    private String operatorName;
    private String reportName;
    private List<String> operatorOutputPorts;

    public RMProcess(String resourceName, String operatorName, String reportName)
    {
        this.resourceName = resourceName;
        this.operatorName = operatorName;
        this.reportName = reportName;
        operatorOutputPorts = new ArrayList<String>();
    }

    public RMProcess(String resourceName, String operatorName, String reportName, List<String> operatorOutputPorts)
    {
        this.resourceName = resourceName;
        this.operatorName = operatorName;
        this.reportName = reportName;
        this.operatorOutputPorts = operatorOutputPorts;
    }

    public void addOperatorOutputPort(String name)
    {
        this.operatorOutputPorts.add(name);
    }

    public String getReportName()
    {
        return reportName;
    }

    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }

    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

    public List<String> getOperatorOutputPorts()
    {
        return operatorOutputPorts;
    }

    public void setOperatorOutputPorts(List<String> operatorOutputPorts)
    {
        this.operatorOutputPorts = operatorOutputPorts;
    }

}
