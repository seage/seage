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
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.ael;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
//import net.sf.saxon.Configuration;
//import net.sf.saxon.PreparedStylesheet;
//import net.sf.saxon.trans.CompilerInfo;

/**
 *
 * @author rick
 */
public class LogAnalyzer0
{
    String _outputPath;
    String _reportPath;

    public LogAnalyzer0(String outputPath, String reportPath)
    {
        _outputPath = outputPath;
        _reportPath = reportPath;
    }

    public static void main(String[] args)
    {
        try
        {

            String in, out;
            if(args.length ==0 )
            {
                String base = "/home/rick/Projects/AISearch/Approaches/SEAGE/Problems/TSP";
                in = base+"/Output.old";
                out = base+"/Statistics";
            }
            else
            {
                in = args[0];
                out = args[1];
            }
            new LogAnalyzer0(in, out).analyze();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    HashMap<String, String> _runDates = new HashMap<String, String>();

    public void analyze() throws Exception
    {
        HashMap<String, ArrayList<String>> outputDataPaths = new HashMap<String, ArrayList<String>>();
        

        File outDir = new File(_outputPath);

        File reportDir = new File(_reportPath);
        if(!reportDir.exists())
            reportDir.mkdirs();
        
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File arg0, String arg1) {
                if(arg1.equals(".svn")) return false;
                else return true;
            }
        };

        // delete all in Statistics directory
        for(String s : reportDir.list(filter))
            new File(_reportPath + "/"+s).delete();

        // list all Output|s directories
        for(String s : outDir.list(filter))
        {
            String config = _outputPath+"/"+ s + "/config.xml";
            if(new File(config).exists())
            {
                String hash = FileHelper.md5fromFile(config);
                ArrayList<String> outputPathList = outputDataPaths.get(hash);
                if(outputPathList == null)
                {
                    outputPathList = new ArrayList<String>();
                    outputDataPaths.put(hash, outputPathList);
                }
                outputPathList.add(s);

                // run sets
                DataNode pureConfig = XmlHelper.readXml(new File(config), null);
                if(pureConfig.getDataNode("agent", 0).containsValue("currentInstance"))
                    pureConfig.getDataNode("agent", 0).putValue("currentInstance", "-");
                if(pureConfig.getDataNode("agent", 1).containsValue("dataPath"))
                    pureConfig.getDataNode("agent", 1).putValue("dataPath", "-");

                String runHash = FileHelper.md5fromString(XmlHelper.getStringFromDocument((pureConfig.toXml())));

                if(!_runDates.containsKey(runHash))
                    _runDates.put(runHash, s);
                else
                {
                    if(_runDates.get(runHash).compareTo(s)==1)
                        _runDates.put(runHash, s);
                }
            }
        }
        DataNode report = new DataNode("report");
        for(String h : outputDataPaths.keySet())
        {
            Collections.sort(outputDataPaths.get(h));

            String reportFileName = outputDataPaths.get(h).get(0) +"-"+ h;
      
            System.out.println(outputDataPaths.get(h).get(0) +"-"+ h);
            report.putDataNode(processDirectorySet(outputDataPaths.get(h), reportDir.getPath(), reportFileName, h));
        }
        //String xslPath = "report.xsl";
        //report.setXslPath(xslPath);
        XmlHelper.writeXml(report, _reportPath+"/report.xml");

        
        //FileHelper.writeToFile(getClass().getResourceAsStream("report.xsl"), new File(_reportPath+"/"+xslPath));
        Templates templates = newTemplates(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report0.xsl")));
        Transformer transformer = templates.newTransformer();


//        Transformer transformer =
//          tFactory.newTransformer
//             (new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report.xsl")));

        transformer.transform
          (new javax.xml.transform.stream.StreamSource
                (_reportPath+"/report.xml"),
           new javax.xml.transform.stream.StreamResult
                ( new FileOutputStream(_reportPath+"/report.html")));

    }
    
    private DataNode processDirectorySet(ArrayList<String> dirs, String destDirPath, String reportFileName, String hash) throws Exception
    {
        
        //FileHelper.copyfile(_outputPath+"/"+dirs.get(0)+"/config.xml", destDirPath+"/config.xml");
        Collections.sort(dirs);

        // TODO: B - add schema validation
        // just for making hash from pure config data to create group directory

        DataNode config = XmlHelper.readXml(new File(_outputPath+"/"+dirs.get(0)+"/config.xml"), null);
        DataNode run = new DataNode("run");
        run.putValue("hash", hash);
        

        String instancePath="";

        if(config.getDataNode("agent", 0).containsValue("currentInstance"))
        {
            instancePath = config.getDataNode("agent", 0).getValueStr("currentInstance");
            config.getDataNode("agent", 0).putValue("currentInstance","-");
        }
        if(config.getDataNode("agent", 1).containsValue("dataPath"))
        {
            instancePath = config.getDataNode("agent", 1).getValueStr("dataPath");
            config.getDataNode("agent", 1).putValue("dataPath","-");
        }
        String instance = instancePath.substring(instancePath.lastIndexOf("/")+1);
        run.putValue("instance", instance);

        Pattern p = Pattern.compile("^[a-zA-Z]+([0-9]+).*");
        Matcher m = p.matcher(instance);
        if (m.find()) {
            run.putValue("size", m.group(1));
        }

        // run set id
        String pureHash = FileHelper.md5fromString(XmlHelper.getStringFromDocument((config.toXml())));
        run.putValue("runID", _runDates.get(pureHash)+"-"+pureHash);

        run.putDataNode(new DataNode("stats"));
        run.putDataNode(config);
        DataNode stats = run.getDataNode("stats");         

        double fitness = 0;
        double fitness2 = 0;
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        int numSolutions = 0;

        for(String srcDirName : dirs)
        {
            try
            {
                System.out.println("\t"+srcDirName);
                String srcDirPath = _outputPath+"/"+srcDirName;

                File xmlFile = null;
                xmlFile = new File(srcDirPath+"/Problem.xml");
                
                if(!xmlFile.exists())
                    xmlFile = new File(srcDirPath+"/TSPSolutionPool.xml");

                if(xmlFile.exists())
                {
                    DataNode data = XmlHelper.readXml(xmlFile);

                    List<DataNode> solList = data.getDataNodes("Solution");
                    if(solList.size() > 0)
                    {
                        double value = solList.get(solList.size()-1).getValueDouble("fitness");
                        fitness += value;
                        fitness2 += value*value;
                        if(value < min)
                            min = value;
                        if(value > max)
                            max = value;

                        numSolutions++;
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        stats.putValue("minFitness", min);
        stats.putValue("avgFitness", fitness/numSolutions);
        stats.putValue("devFitness", Math.sqrt(fitness2/numSolutions - Math.pow(stats.getValueDouble("avgFitness"), 2)));
        stats.putValue("cfvFitness", stats.getValueDouble("devFitness") / stats.getValueDouble("avgFitness"));
        stats.putValue("maxFitness", max);
        stats.putValue("numOfRuns", numSolutions);

        XmlHelper.writeXml(run, destDirPath+"/"+reportFileName+".xml");
        return run;
    }

    protected Templates newTemplates(Source source) throws TransformerConfigurationException {
//        Configuration config = new Configuration();
//        CompilerInfo info = new CompilerInfo();
//        info.setURIResolver(config.getURIResolver());
//        info.setErrorListener(config.getErrorListener());
//        info.setCompileWithTracing(config.isCompileWithTracing());
//
//        return PreparedStylesheet.compile(source, config, info);
        return null;
    }


}


