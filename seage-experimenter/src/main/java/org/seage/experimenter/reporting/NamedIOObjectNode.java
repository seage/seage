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

import com.rapidminer.operator.IOObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
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
