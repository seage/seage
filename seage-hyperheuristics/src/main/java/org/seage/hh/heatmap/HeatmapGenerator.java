/**
 * Program generates a heatmap, visualizating given data from experiment
 * 
 * @author David Omrai
 */

package org.seage.hh.heatmap;

import org.seage.hh.experimenter.ExperimentScoreCard;
import com.hubspot.jinjava.Jinjava;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// ---------------------------------------------------

import net.mahdilamb.colormap.SequentialColormap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeatmapGenerator {
  /**
   * Class represents the structure of json data
   */
  public static class ExperimentScoreCards {
    public List<ExperimentScoreCard> results;

    public ExperimentScoreCards() {
      this.results = new ArrayList<>();
    }
  }

  // Path where the metadata are stored
  private static final String SVG_TEMPLATE_PATH = "heatmap.svg.template";

  // Gradient colors
  private static Color[][] gradColors = {{new Color(140, 0, 0), new Color(255, 0, 0)}, // dark red - red
      {new Color(255, 0, 0), new Color(255, 250, 0)}, // red - yellow
      {new Color(255, 250, 0), new Color(1, 150, 32)}, // yellow - green
      {new Color(1, 150, 32), new Color(1, 120, 32)}, // green - dark green
  };
  // Gradient maps - for each two colors on the spectrum
  private static SequentialColormap[] gradMaps =
      {new SequentialColormap(gradColors[0]), new SequentialColormap(gradColors[1]),
          new SequentialColormap(gradColors[2]), new SequentialColormap(gradColors[3])};
  // Gradient color borders
  static double[] gradBorders = {0.5, 0.75, 0.98, 1.0};

  /**
   * Class represents a structure where are data about problem results stored.
  */
  protected static class AlgorithmProblemResult {
    String name;
    double score;
    Color color;
    // Red color 0-255
    int redColor;
    // Green color 0-255
    int greenColor;
    // Blue color 0-255
    int blueColor;
  }

  /**
   * Class represents a structure where are data about overall algorithm stored.
   */
  protected static class AlgorithmResult {
    String name;
    double score;
    String author;
    Color color;
    int redColor;
    int greenColor;
    int blueColor;
    // problem instances results
    HashMap<String, AlgorithmProblemResult> problemsResults;
  }

  /**
   * Empty constructor.
   */
  private HeatmapGenerator() {
    // Empty
  }

  /**
   * Method turns the given position into a specific color based on the location on the gradient.
   * 
   * @param pos double value in range [0,1]
   * @return A appropriate color on the gradient
   */
  protected static Color getColor(Double pos) {
    // Find appropriate gradient
    int colPos;
    for (colPos = 0; colPos < gradBorders.length; colPos++) {
      if (pos <= gradBorders[colPos]) {
        break;
      }
    }

    // Scale old position
    double fromPos = colPos == 0 ? 0.0 : gradBorders[colPos - 1];
    double toPos = gradBorders[colPos];

    double newPos = (pos - fromPos) / (toPos - fromPos);

    // Return the color
    return gradMaps[colPos].get(newPos);
  }

  /**
   * Method reads the problems that appears in the results and stores them into a problem array.
   */
  protected static List<String> getProblemsNames(List<AlgorithmResult> results) {
    // Extract the problems names
    HashSet<String> problems = new HashSet<>();
    for (AlgorithmResult result : results) {
      problems.addAll(result.problemsResults.keySet());
    }
    List<String> problemsList = new ArrayList<>(problems);
    // Sort the problem names
    Collections.sort(problemsList);
    return problemsList;
  }

  /**
   * Method sorts the results list using the hhs overall scores.
   */
  protected  static List<AlgorithmResult> sortResults(List<AlgorithmResult> results) {
    // Sort the results by their overall score
    Collections.sort(results, (var lar, var rar) -> Double.compare(rar.score, lar.score));
    return results;
  }

  /**
   * Method loads the xml file.
   * 
   * @param xmlPath path to the xml file
   */
  protected static void loadXmlFile(
      String xmlPath, List<AlgorithmResult> results, Map<String, String> algAuthors
  ) throws Exception {
    
    // Read the xml file
    File xmlFile = new File(xmlPath);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(xmlFile);

    // Normalize the xml structure
    doc.getDocumentElement().normalize();

    // Element
    Element root = doc.getDocumentElement();
    // Get the algorithms elements
    NodeList algorithmsXml = root.getElementsByTagName("algorithm");

    // For all algorithms results
    for (int i = 0; i < algorithmsXml.getLength(); i++) {
      // Get the algorithm results
      Node algorithmNode = algorithmsXml.item(i);

      // Get the algorithm details
      if (algorithmNode.getNodeType() == Node.ELEMENT_NODE) {
        Element algorithmElement = (Element) algorithmNode;
        AlgorithmResult result = new AlgorithmResult();

        // Add each result into a new class and put it all into array or map
        result.name = algorithmElement.getAttribute("name");
        result.score = Double.parseDouble(String.format("%.5f",
            Double.parseDouble(algorithmElement.getAttribute("score"))));
        result.author =
            algAuthors.containsKey(result.name) ? algAuthors.get(result.name) : "";
        result.color = getColor(result.score);
        result.redColor = result.color.getRed();
        result.greenColor = result.color.getGreen();
        result.blueColor = result.color.getBlue();

        // Extract the algorithm results of each problem domain
        NodeList problemsXml = algorithmElement.getElementsByTagName("problem");

        result.problemsResults = new HashMap<>();

        for (int problemId = 0; problemId < problemsXml.getLength(); problemId++) {
          Node problemNode = problemsXml.item(problemId);

          if (problemNode.getNodeType() == Node.ELEMENT_NODE) {
            Element problemElement = (Element) problemNode;

            // Create new structure
            AlgorithmProblemResult newRes = new AlgorithmProblemResult();

            // Set the problem result parameters
            newRes.name = problemElement.getAttribute("name");
            newRes.score = Double.parseDouble(String.format("%.5f",
                Double.parseDouble(problemElement.getAttribute("avg"))));
            newRes.color = getColor(newRes.score);
            newRes.redColor = newRes.color.getRed();
            newRes.greenColor = newRes.color.getGreen();
            newRes.blueColor = newRes.color.getBlue();
            
            // Add new problem results to algorithm
            result.problemsResults.put(newRes.name, newRes);
          }
        }
        results.add(result);
      }
    }
  }

  protected static AlgorithmResult getAlgorithmResult (ExperimentScoreCard scoreCard, Map<String, String> algAuthors) {
      AlgorithmResult result = new AlgorithmResult();

      // Store the result informations
      result.name = scoreCard.getAlgorithmName();
      result.score = Double.parseDouble(String.format("%.5f",
          scoreCard.getAlgorithmScore()));
      result.author =
          algAuthors.containsKey(result.name) ? algAuthors.get(result.name) : "";
      result.color = getColor(result.score);
      result.redColor = result.color.getRed();
      result.greenColor = result.color.getGreen();
      result.blueColor = result.color.getBlue();

      // Get the problem results
      result.problemsResults = new HashMap<>();

      for (String problemID : scoreCard.getProblems()) {
        // Create new structure
        AlgorithmProblemResult newRes = new AlgorithmProblemResult();

        // Set the problem result parameters
        newRes.name = problemID;
        newRes.score = Double.parseDouble(String.format("%.5f",
            scoreCard.getProblemScore(problemID)));
        newRes.color = getColor(newRes.score);
        newRes.redColor = newRes.color.getRed();
        newRes.greenColor = newRes.color.getGreen();
        newRes.blueColor = newRes.color.getBlue();
        
        // Add new problem results to algorithm
        result.problemsResults.put(newRes.name, newRes);
      }
        return result;
  }

  protected static List<ExperimentScoreCard> loadJson(
      String jsonString) {
    // Read the json string
    Gson gson = new Gson();
    Type listType = new TypeToken<ExperimentScoreCards>() {}.getType();
    ExperimentScoreCards experimentResults = gson.fromJson(jsonString, listType);
    
    return experimentResults.results;
  }

  protected static List<AlgorithmResult> loadExperimentScoreCards(
      List<ExperimentScoreCard> expScoreCards, Map<String, String> algAuthors) {
    List<AlgorithmResult> results = new ArrayList<>();
    for ( ExperimentScoreCard expScoreCard : expScoreCards) {
      // Get json result data
      results.add(getAlgorithmResult(expScoreCard, algAuthors));
    }
    return results;
  }
  /**
   * Method turns the structures in given lists into a arrays, that can be read by jinja.
   * 
   * @param results list of algorithm results (with colors)
   * @param problems list of problems
   * @param algsOverRes list of lists of algorithm's intances results 
   * @param algsProbsRes list of algorithms problems results 
   */
  protected static void resultsToList(
      List<AlgorithmResult> results, List<String> problems,
      List<List<String>> algsOverRes, List<List<List<String>>> algsProbsRes
  ) {
    for (int i = 0; i < results.size(); i++) {
      // Initialize list for algorithm results
      List<String> algOverRes = new ArrayList<>();

      // Store results
      AlgorithmResult algRes = results.get(i);
      algOverRes.add(algRes.name);
      algOverRes.add(algRes.author);
      algOverRes.add(String.valueOf(algRes.score));
      algOverRes.add(String.valueOf(algRes.redColor));
      algOverRes.add(String.valueOf(algRes.greenColor));
      algOverRes.add(String.valueOf(algRes.blueColor));

      // Loop over problems results
      List<List<String>> algProbsRes = new ArrayList<>();
      for (int j = 0; j < problems.size(); j++) {
        List<String> algProbRes =  Arrays.asList(new String[5]);

        AlgorithmProblemResult probRes = algRes.problemsResults.get(problems.get(j));
        algProbRes.set(0, problems.get(j));
        if (probRes != null) {
          algProbRes.set(1, String.valueOf(probRes.score));
          algProbRes.set(2, String.valueOf(probRes.redColor));
          algProbRes.set(3, String.valueOf(probRes.greenColor));
          algProbRes.set(4, String.valueOf(probRes.blueColor));
        }
        // Add to problems results array
        algProbsRes.add(algProbRes);
      }
      // Store the results
      algsOverRes.add(algOverRes);
      algsProbsRes.add(algProbsRes);
    }
  }

  /**
   * Method generates a svg string with given data.
   * 
   * @param experimentTag id of the experiment
   * @param results list of algorithm results (with colors)
   * @param problems list of problems
   * @throws IOException exception if the page couldn't be created
   */
  protected static String createSvgString(
      String experimentTag, List<AlgorithmResult> results, List<String> problems
  ) throws IOException {
    // Get the transformed data
    List<List<String>> algsOverRes = new ArrayList<>();
    List<List<List<String>>> algsProbsRes = new ArrayList<>();
    resultsToList(results, problems, algsOverRes, algsProbsRes);
    // Get the current time
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());

    // Create context
    Map<String, Object> context = new HashMap<>();
    context.put("overallResults", algsOverRes);
    context.put("problemsResults", algsProbsRes);
    context.put("problems", problems);
    context.put("tag", experimentTag);
    context.put("datetime", formatter.format(date));
    context.put("height", algsOverRes.size() * 40 + 50);

    // Load the jinja svg template
    try (
        InputStream svgTemplateStream = HeatmapGenerator
            .class.getResourceAsStream(SVG_TEMPLATE_PATH)
    ) {
      String svgTemplate = new String(svgTemplateStream.readAllBytes(), StandardCharsets.UTF_8);
      // Render the template
      Jinjava jinjava = new Jinjava();
      // Output the file
      return jinjava.render(svgTemplate, context);
    }
  }

  /**
   * Method generates a svg file with given data.
   * 
   * @param experimentTag id of the experiment
   * @param results list of algorithm results (with colors)
   * @param svgFileDest svg file destination
   * @throws IOException exception if the page couldn't be created
   */
  protected static void createSvgFile(
      String experimentTag, List<AlgorithmResult> results, String svgFileDest
  ) throws IOException {
    // Get the problems
    List<String> problems = getProblemsNames(results);
    try (FileWriter fileWriter = new FileWriter(svgFileDest);) {
      fileWriter.write(createSvgString(experimentTag, results, problems));
    }
  }

  /**
   * Method receives neccesary data and create the result svg file.
   * @param table list of experiments score cards
   * @param experimentTag id of the competition experiment
   * @param algAuthors map of algorithm's authors
   */
  public static String createHeatmap(
      List<ExperimentScoreCard> table, String experimentTag, Map<String, String> algAuthors
  ) throws IOException {
    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(table, algAuthors);
    // Get the problems
    List<String> problems = getProblemsNames(results);
    // Sort the results
    HeatmapGenerator.sortResults(results);
    // Return the SVG string
    return HeatmapGenerator.createSvgString(experimentTag, results, problems);
  }
}

