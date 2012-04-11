package org.seage.ael;

/**
 *
 * @author Rick
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