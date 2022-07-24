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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams.dummy;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy;

/**
 * Dummy hypotheses generates random parameters at the beginning (in constructor, if desired)
 * and the parameters are not changed during suggestions.
 * @author Richard Malek
 */
public class DummyPolicy extends Policy
{
    protected int _id;

    public DummyPolicy()
    {
        super(null, null);
        _id = hashCode();

    }

    @Override
    public DataNode suggest(AlgorithmReport report) throws Exception
    {
        super.suggest(report);

        DataNode result = report.getDataNode("parameters");

        result.putValue("continue", false);
        
        return result;
    }

    public int getID()
    {
        return _id;
    }
}
