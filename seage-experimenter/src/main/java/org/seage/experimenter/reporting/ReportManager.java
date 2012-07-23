/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
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
