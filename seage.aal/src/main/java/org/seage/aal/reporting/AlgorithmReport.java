/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.reporting;

import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class AlgorithmReport extends DataNode
{
    private int _id;
    public AlgorithmReport(String name) {
        super(name);
    }

    public int getId()
    {
        return _id;
    }

    public void setId(int id)
    {
        _id = id;
        putValue("id", id);
    }

}
