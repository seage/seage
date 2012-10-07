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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.experimenter.config;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

/**
 * 
 * @author rick
 */
public abstract class Configurator
{
    // TODO: A - Remove this method, replace it by the second one.
    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception;

    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, DataNode instanceInfo, int numConfigs) throws Exception;

    // TODO: A - modify the class to be a config stream ...
    // - to provide (generate) configs through a method 'next'
    //
}
