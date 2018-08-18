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

package org.seage.ael;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

/**
 *
 * @author Richard Malek
 */
public class LogAnalyzer
{
    String _outputPath;
    String _reportPath;

    public LogAnalyzer(String outputPath, String reportPath)
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
                in = base+"/Output";
                out = base+"/Statistics";
            }
            else
            {
                in = args[0];
                out = args[1];
            }
            new LogAnalyzer(in, out).analyze();
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

        DataNode report = new DataNode("report");
        // list all Output|s directories
        for(String s : outDir.list(filter))
        {
            String config = _outputPath+"/"+ s + "/config.xml";
            if(new File(config).exists())
            {
                List<DataNode> runs = processDirectorySet(new File(_outputPath+"/"+ s).listFiles(), _outputPath, _reportPath, s);

                DataNode batch = new DataNode("batch");
                for(DataNode dn : runs)
                    batch.putDataNode(dn);

                batch.putValue("id", s);

                String[] date = s.split("-");
                batch.putValue("date", date[0]+"-"+date[1]);
                batch.putDataNode(XmlHelper.readXml(new File(config)));

                report.putDataNode(batch);
            }
        }

        XmlHelper.writeXml(report, _reportPath+"/report.xml");

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report.xsl")));

        transformer.transform
          (new javax.xml.transform.stream.StreamSource
                (_reportPath+"/report.xml"),
           new javax.xml.transform.stream.StreamResult
                ( new FileOutputStream(_reportPath+"/report.html")));

    }
    
    private List<DataNode> processDirectorySet(File[] files, String destDirPath, String reportFileName, String hash) throws Exception
    {
        HashMap<String, List<Double>> _fitnesses = new HashMap<String, List<Double>>();

        for(File d : files)
        {
            if(!d.isDirectory())
                continue;

            File problemXml = new File(d.getPath()+"/Problem.xml");

            if(problemXml.exists())
            {
                DataNode data = XmlHelper.readXml(problemXml);
                List<DataNode> solList = data.getDataNodes("Solution");
                if(solList.size() > 0)
                {
                    double fitness = solList.get(solList.size()-1).getValueDouble("fitness");
                    String instance = data.getValueStr("instance");
                    if(!_fitnesses.containsKey(instance))
                        _fitnesses.put(instance, new ArrayList<Double>());
                    _fitnesses.get(instance).add(fitness);
                }
            }
        }

        ArrayList<DataNode> runSet = new ArrayList<DataNode>();

        for(String in : _fitnesses.keySet())
        {
            DataNode run = new DataNode("run");
            run.putValue("instance", in);

            Pattern p = Pattern.compile("^[a-zA-Z]+([0-9]+).*");
            Matcher m = p.matcher(in);
            if (m.find()) {
                run.putValue("size", m.group(1));
            }

            run.putDataNode(new DataNode("stats"));

            DataNode stats = run.getDataNode("stats");

            double fitness = 0;
            double fitness2 = 0;
            double min = Integer.MAX_VALUE;
            double max = Integer.MIN_VALUE;
            int numSolutions = 0;

            for(Double f : _fitnesses.get(in))
            {
                fitness += f;
                fitness2 += f*f;
                if(f < min)
                    min = f;
                if(f > max)
                    max = f;

                numSolutions++;

            }

            stats.putValue("minFitness", min);
            stats.putValue("avgFitness", fitness/numSolutions);
            stats.putValue("devFitness", Math.sqrt(fitness2/numSolutions - Math.pow(stats.getValueDouble("avgFitness"), 2)));
            stats.putValue("cfvFitness", stats.getValueDouble("devFitness") / stats.getValueDouble("avgFitness"));
            stats.putValue("maxFitness", max);
            stats.putValue("numOfRuns", numSolutions);

            runSet.add(run);
            //XmlHelper.writeXml(run, destDirPath+"/"+reportFileName+".xml");
        }
        return runSet;
    }
}
