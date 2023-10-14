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
  private static final Logger log = 
      LoggerFactory.getLogger(ProblemMetadataGenerator.class.getName());

  private static final String INSTANCES_STRING = "Instances";
  private static final String OPTIMUM_STRING = "optimum";

  protected ProblemProvider<P> problemProvider;

  protected abstract double generateRandomSolutionValue(
      ProblemInstance instance, IPhenotypeEvaluator<P> evaluator) throws Exception;

  protected abstract double generateGreedySolutionValue(
      ProblemInstance instance, IPhenotypeEvaluator<P> evaluator) throws Exception;

  protected abstract Map<String, Double> getOptimalValues() throws Exception;

  protected ProblemMetadataGenerator(ProblemProvider<P> problemProvider) {
    this.problemProvider = problemProvider;
  }

  public void runMetadataGenerator(int numberOfTrials) throws Exception {
    ProblemInfo pi = problemProvider.getProblemInfo();
    String problemID = pi.getProblemID();
     
    log.info("Working on {} problem...", problemID);     

    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemID);

    problem.putDataNode(createInstancesMetadata(numberOfTrials));

    saveToFile(problem, problemID.toLowerCase());
  }

  private DataNode createInstancesMetadata(int numberOfTrials) throws Exception {
    DataNode result = new DataNode(INSTANCES_STRING);
    ProblemInfo pi = this.problemProvider.getProblemInfo();
    List<ProblemInstanceInfo> instances =
        problemProvider.getProblemInfo().getProblemInstanceInfos();
    Map<String, Double> optimumResults = getOptimalValues();

    DecimalFormat df = new DecimalFormat("#");
    df.setMaximumFractionDigits(8);

    Collections.sort(instances, (i1, i2) -> (i1.getInstanceID().compareTo(i2.getInstanceID())));

    // iterate through all instances
    for (ProblemInstanceInfo pii : instances) {
      String instanceID = pii.getInstanceID();
      try {
        log.info("Processing: {}", instanceID);

        ProblemInstance instance = this.problemProvider.initProblemInstance(pii);
        IPhenotypeEvaluator<P> evaluator = this.problemProvider.initPhenotypeEvaluator(instance);
        double[] randomResults = new double[numberOfTrials];
        double greedyResult = Double.MAX_VALUE;
        List<Integer> indexes = new ArrayList<>(numberOfTrials);

        for (int i = 0; i < numberOfTrials; i++) {
          indexes.add(i);
        }

        // Get greedy solution value
        log.info("Greedy solutions for: {}", instanceID);
        greedyResult = generateGreedySolutionValue(instance, evaluator);

        // Get random solution values
        log.info("Random solutions for: {}", instanceID);
        indexes.parallelStream().forEach((i) -> {
          try {            
            randomResults[i] = generateRandomSolutionValue(instance, evaluator);
          } catch (Exception ex) {
            log.warn("Processing trial error", ex);
          }
        });

        DataNode inst = new DataNode("Instance");
        inst.putValue("id", pi.getDataNode(INSTANCES_STRING)
            .getDataNodeById(instanceID).getValue("id"));
        inst.putValue("greedy", (int) greedyResult);
        inst.putValue("random", (int) median(randomResults));

        if (optimumResults.containsKey(instanceID.toLowerCase())) {
          inst.putValue(OPTIMUM_STRING, df.format(optimumResults.get(instanceID.toLowerCase())));
        } else {
          inst.putValue(OPTIMUM_STRING, "TBA");
        }

        inst.putValue("size", instance.getSize());
        result.putDataNode(inst);
      } catch (Exception ex) {
        log.warn("Instance error, {}: {}", instanceID, ex.getMessage());
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
            Double.parseDouble(optimalValue.getValue(OPTIMUM_STRING).toString()));
      }
    }

    return results;
  }

  private DataNode readOptimalValueLine(String line) {
    try (Scanner scanner = new Scanner(line)) {      
      scanner.useDelimiter(" : ");

      DataNode result = new DataNode("Optimal");
      result.putValue("name", scanner.next().substring(2).toLowerCase());
      result.putValue(OPTIMUM_STRING, scanner.next());

      return result;
    } catch (Exception ex) {
      log.error("Error reading line: {}", line);
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

  /**
   * Method stores given data into a xml file.
   * 
   * @param dn DataNode object with data for outputting.
   */
  private static void saveToFile(DataNode dn, String problemName) throws Exception {
    File directory = new File("./output");
    if (! directory.exists()) {
      directory.mkdir();
    }
    File path = new File("./output/" + problemName + ".metadata.xml");

    log.info("Saving the results to the file {}", path.getAbsolutePath());
    
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
