package org.seage.aal.problem;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProblemMetadataGenerator<P extends Phenotype<?>> {
  private static final Logger logger = LoggerFactory.getLogger(ProblemMetadataGenerator.class.getName());

  protected ProblemProvider<P> problemProvider;

  protected abstract double generateRandomSolutionValue(ProblemInstance instance, IPhenotypeEvaluator<P> evaluator) throws Exception;
  protected abstract double generateGreedySolutionValue(ProblemInstance instance, IPhenotypeEvaluator<P> evaluator) throws Exception;
  protected abstract Map<String, Double> getOptimalValues() throws Exception;

  public ProblemMetadataGenerator(ProblemProvider<P> problemProvider) {
    this.problemProvider = problemProvider;
  }

  public void runMetadataGenerator(int numberOfTrials) throws Exception {
    ProblemInfo pi = problemProvider.getProblemInfo();
    String problemID = pi.getProblemID();
     
    logger.info("Working on {} problem...", problemID);     

    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemID);

    problem.putDataNode(createInstancesMetadata(numberOfTrials));

    saveToFile(problem, problemID.toLowerCase());
  }

  private DataNode createInstancesMetadata(int numberOfTrials) throws Exception {
    DataNode result = new DataNode("Instances");
    ProblemInfo pi = this.problemProvider.getProblemInfo();
    Map<String, Double> optimumResults = getOptimalValues();

    DecimalFormat df = new DecimalFormat("#");
    df.setMaximumFractionDigits(8);

    // iterate through all instances
    for (String instanceID : getSortedInstanceIDs(pi)) {
      try {
        logger.info("Processing: {}", instanceID);

        ProblemInstanceInfo pii = pi.getProblemInstanceInfo(instanceID);
        ProblemInstance instance = this.problemProvider.initProblemInstance(pii);
        IPhenotypeEvaluator<P> evaluator = this.problemProvider.initPhenotypeEvaluator(instance);
        double[] randomResults = new double[numberOfTrials];
        double[] greedyResults = new double[numberOfTrials];
        List<Integer> indexes = new ArrayList<>(numberOfTrials);

        for (int i = 0; i < numberOfTrials; i++) {
          indexes.add(i);
        }

        indexes.parallelStream().forEach((i) -> {
          try {
            logger.info("Greedy for: {}, trial {}", instanceID, i+1);
            greedyResults[i] = generateGreedySolutionValue(instance, evaluator);
          } catch (Exception ex) {
            logger.warn("Processing trial error", ex);
          }
        });

        indexes.parallelStream().forEach((i) -> {
          try {
            logger.info("Random for: {}, trial {}", instanceID, i+1);
            randomResults[i] = generateRandomSolutionValue(instance, evaluator);
          } catch (Exception ex) {
            logger.warn("Processing trial error", ex);
          }
        });

        DataNode inst = new DataNode("Instance");
        inst.putValue("id", pi.getDataNode("Instances").getDataNodeById(instanceID).getValue("id"));
        inst.putValue("greedy", (int) median(greedyResults));
        inst.putValue("random", (int) median(randomResults));

        if (optimumResults.containsKey(instanceID.toLowerCase())) {
          inst.putValue("optimum", df.format(optimumResults.get(instanceID.toLowerCase())));
        } else {
          inst.putValue("optimum", "TBA");
        }

        inst.putValue("size", instance.getSize());
        result.putDataNode(inst);
      } catch (Exception ex) {
        logger.warn("Instance error, {}: {}", instanceID, ex.getMessage());
      }
    }

    return result;
  }

  protected Map<String, Double> getOptimalValues(String resourcePath) throws Exception {
    HashMap<String, Double> results = new HashMap<>();

    // Get optimal value
    try (Scanner scanner = new Scanner(getClass().getResourceAsStream(resourcePath))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank() || line.isEmpty()) {
          continue;
        }
        if (line.equals("EOF")) {
          break;
        }

        DataNode optimalValue = readOptimalValueLine(line);

        results.put(optimalValue.getValue("name").toString(),
            Double.parseDouble(optimalValue.getValue("optimum").toString()));
      }
    }

    return results;
  }

  private DataNode readOptimalValueLine(String line) {
    try (Scanner scanner = new Scanner(line)) {      
      scanner.useDelimiter(" : ");

      DataNode result = new DataNode("Optimal");
      result.putValue("name", scanner.next().substring(2).toLowerCase());
      result.putValue("optimum", scanner.next());

      return result;
    } catch(Exception ex) {
      logger.error("Error reading line: " + line);
      throw ex;
    }
  }

  /**
   * Method takes array, finds and returns the median.
   * 
   * @param array array of doubles.
   * @return median of given array
   */
  private static double median(double[] array) {
    if (array.length == 1) {
      return array[0];
    }

    Arrays.sort(array);
    if (array.length % 2 == 0) {
      return (((double) array[array.length / 2] + (double) array[array.length / 2 - 1]) / 2);
    }
    return ((double) array[(array.length / 2)]);
  }

  private List<String> getSortedInstanceIDs(ProblemInfo pi) throws Exception {
    List<String> instanceIDs = new ArrayList<>();
    for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
      instanceIDs.add(inst.getValueStr("id"));
    }

    Collections.sort(instanceIDs);
    return instanceIDs;
  }

  /**
   * Method stores given data into a xml file.
   * 
   * @param dn DataNode object with data for outputting.
   */
  private static void saveToFile(DataNode dn, String problemName) throws Exception {
    File directory = new File("./output");
    if (! directory.exists()){
        directory.mkdir();
    }
    File path = new File("./output/" + problemName + ".metadata.xml");

    logger.info("Saving the results to the file {}", path.getAbsolutePath());
    
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Compliant
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource domSource = new DOMSource(dn.toXml());
    StreamResult streamResult = new StreamResult(path);

    transformer.transform(domSource, streamResult);
  }
}
