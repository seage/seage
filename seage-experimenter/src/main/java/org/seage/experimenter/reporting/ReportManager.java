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
package org.seage.experimenter.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zmatlja1
 */
public class ReportManager {
    
    List<ILogReport> _reporters;

    public ReportManager()
    {
        this._reporters = new ArrayList<ILogReport>();
    }
    
    public void addReporter(ILogReport reporter)
    {
        this._reporters.add( reporter );
    }
    
    public List<ILogReport> getReporters()
    {
        return _reporters;
    }
    
    public void executeAllReporters() throws Exception
    {
        for(ILogReport reporter : _reporters)
            reporter.report();
    }
    
}
