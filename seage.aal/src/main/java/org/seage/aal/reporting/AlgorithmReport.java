/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.reporting;

import java.io.File;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

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

    public void save(String path)
    {
        File f = new File(path);
        if(!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        XmlHelper.writeXml(this, path);
    }
}
