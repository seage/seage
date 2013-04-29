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

import com.rapidminer.operator.IOObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
/**
 * Internal structure for RapidMiner proces, that holds a data from output ports and converted DataNode made from them
 */
public class NamedIOObjectNode
{
    private HashMap<String, IOObject> map;
    private List<DataNode> dataNodes;
    private RMProcess rmProcess;

    public NamedIOObjectNode() {
        this.map = new HashMap<String, IOObject>();
        this.dataNodes = new ArrayList<DataNode>();
    }
    
    public void addDataFromOutputPort(String port, IOObject data)
    {
        this.map.put( port , data );
    }
    
    public void addDataNode(DataNode dataNode)
    {
        this.dataNodes.add( dataNode );
    }

    public List<DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(List<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public RMProcess getRmProcess() {
        return rmProcess;
    }

    public void setRmProcess(RMProcess rmProcess) {
        this.rmProcess = rmProcess;
    }


    public HashMap<String, IOObject> getMap() {
        return map;
    }

    public void setMap(HashMap<String, IOObject> map) {
        this.map = map;
    }

}
