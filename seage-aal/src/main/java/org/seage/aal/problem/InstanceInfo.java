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

package org.seage.aal.problem;

import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
public class InstanceInfo extends DataNode
{
	private static final long serialVersionUID = 1L;
	
	protected String _instanceID;
        
    public InstanceInfo(DataNode instance) throws Exception
    {
    	super(instance);
    	_instanceID = getValueStr("id");
    }

    public String getInstanceID() {
        return _instanceID;
    }
    
    @Override
    public String toString() {
        return _instanceID;
    }
    
    
}
