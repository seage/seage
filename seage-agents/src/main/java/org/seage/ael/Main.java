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
package org.seage.ael;

/**
 *
 * @author Richard Malek
 */
import aglobe.platform.Platform;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import java.io.File;
import java.io.InputStream;


public class Main
{
    public static void main(String[] args)
    {
        try
        {
//            if(args.length == 0 || !new File(args[0]).exists())
//                throw new Exception("Config path expected.");
            new Main().runPlatform();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void runPlatform() throws Exception
    {
        String schemaPath = "config.xsd";
        InputStream schema = Main.class.getResourceAsStream(schemaPath);
        if(schema == null)
            throw new Exception("Unable to load schema: " + schemaPath);
        // read config file
        DataNode ds = XmlHelper.readXml(new File("config-agents.xml"), schema);
        DataNode problemNode = ds.getDataNode("agent", 0);

        String problemName = problemNode.getValueStr("name");
        String agentClass = problemNode.getValueStr("mainClass");

        Platform.main(new String[]{"-name", problemName, "-topics", "-gui", problemName+":"+agentClass});
    }
}
