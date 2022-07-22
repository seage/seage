/**
 * Program generates a heatmap, visualizating given data from experiment
 * 
 * @author David Omrai
 */

package org.seage.aal.heatmap;

import com.hubspot.jinjava.Jinjava;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// ---------------------------------------------------

import net.mahdilamb.colormap.SequentialColormap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeatmapGenerator {
  // Path where the metadata are stored
  String templatePath = "/heatmap.svg.template";

  // Gradient colors
  private Color[][] gradColors = {{new Color(140, 0, 0), new Color(255, 0, 0)}, // dark red - red
      {new Color(255, 0, 0), new Color(255, 250, 0)}, // red - yellow
      {new Color(255, 250, 0), new Color(1, 150, 32)}, // yellow - green
      {new Color(1, 150, 32), new Color(1, 120, 32)}, // green - dark green
  };
  // Gradient maps - for each two colors on the spectrum
  private SequentialColormap[] gradMaps =
      {new SequentialColormap(gradColors[0]), new SequentialColormap(gradColors[1]),
          new SequentialColormap(gradColors[2]), new SequentialColormap(gradColors[3])};
  // Gradient color borders
  double[] gradBorders = {0.5, 0.75, 0.98, 1.0};

  /**
   * Class represents a structure where are data about problem results stored.
  */
  protected class AlgorithmProblemResult {
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
  protected class AlgorithmResult {
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
   * Method turns the given position into a specific color based on the location on the gradient.
   * 
   * @param pos double value in range [0,1]
   * @return A appropriate color on the gradient
   */
  protected Color getColor(Double pos) {
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
  protected List<String> getProblemsNames(List<AlgorithmResult> results) {
    // Extract the problems names
    List<String> problems = results.isEmpty() ? new ArrayList<>()
        : new ArrayList<>(results.get(0).problemsResults.keySet());
    // Sort the problem names
    Collections.sort(problems);
    return problems;
  }

  /**
   * Method sorts the results list using the hhs overall scores.
   */
  protected void sortResults(List<AlgorithmResult> results) {
    // Sort the results by their overall score
    Collections.sort(results, (var lar, var rar) -> Double.compare(rar.score, lar.score));
  }

  /**
   * Method loads the xml file.
   * 
   * @param xmlPath path to the xml file
   */
  protected void loadXmlFile(
      String xmlPath, List<AlgorithmResult> results, Map<String, String> algAuthors
  ) throws Exception {
    
    // Read the xml file
    File xmlFile = new File(xmlPath);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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

  protected List<AlgorithmResult> loadJson(
      InputStream jsonInputStream, Map<String, String> algAuthors) {
    // Initialize the results
    List<AlgorithmResult> results = new ArrayList<>();

    // Read the xml file
    JSONTokener tokener = new JSONTokener(jsonInputStream);
    JSONObject object = new JSONObject(tokener);

    // Iterate through results
    JSONArray resultsJson = object.getJSONArray("results");
    for (int i = 0; i < resultsJson.length(); i++) {
      // Get json result data
      JSONObject resultJson = resultsJson.getJSONObject(i);
      AlgorithmResult result = new AlgorithmResult();

      // Store the result informations
      result.name = resultJson.getString("algorithmName");
      result.score = Double.parseDouble(String.format("%.5f",
          resultJson.getDouble("totalScore")));
      result.author =
          algAuthors.containsKey(result.name) ? algAuthors.get(result.name) : "";
      result.color = getColor(result.score);
      result.redColor = result.color.getRed();
      result.greenColor = result.color.getGreen();
      result.blueColor = result.color.getBlue();

      // Get the problem results
      JSONObject problemsJson = resultJson.getJSONObject("scorePerProblem");
      result.problemsResults = new HashMap<>();

      for (String key : problemsJson.keySet()) {
        // Create new structure
        AlgorithmProblemResult newRes = new AlgorithmProblemResult();

        // Set the problem result parameters
        newRes.name = key;
        newRes.score = Double.parseDouble(String.format("%.5f",
            problemsJson.getDouble(key)));
        newRes.color = getColor(newRes.score);
        newRes.redColor = newRes.color.getRed();
        newRes.greenColor = newRes.color.getGreen();
        newRes.blueColor = newRes.color.getBlue();
        
        // Add new problem results to algorithm
        result.problemsResults.put(newRes.name, newRes);
      }
      results.add(result);
    }
    
    return results;
  }

  /**
   * Method turns the structures in given lists into a arrays, that can be read by jinja.
   * 
   */
  protected void resultsToList(
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
        List<String> algProbRes = new ArrayList<>();

        AlgorithmProblemResult probRes = algRes.problemsResults.get(problems.get(j));
        algProbRes.add(probRes.name);
        algProbRes.add(String.valueOf(probRes.score));
        algProbRes.add(String.valueOf(probRes.redColor));
        algProbRes.add(String.valueOf(probRes.greenColor));
        algProbRes.add(String.valueOf(probRes.blueColor));

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
   * @param id id of the experiment
   * @throws IOException exception if the page couldn't be created
   */
  protected String createSvgString(
      String experimentId, List<AlgorithmResult> results, List<String> problems
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
    context.put("experimentId", experimentId);
    context.put("datetime", formatter.format(date));

    // Load the jinja svg template
    try (InputStream svgTemplateStream = HeatmapGenerator.class.getResourceAsStream(templatePath)) {
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
   * @param id id of the experiment
   * @throws IOException exception if the page couldn't be created
   */
  protected void createSvgFile(
      String experimentId, List<AlgorithmResult> results, List<String> problems,
      String svgFileDest
  ) throws IOException {
    try (FileWriter fileWriter = new FileWriter(svgFileDest);) {
      fileWriter.write(createSvgString(experimentId, results, problems));
    }
  }

  /**
   * Method receives neccesary data and create the result svg file.
   * @param experimentId id of the competition experiment
   * @param algAuthors map of algorithm authors
   */
  public String createHeatmap(
      InputStream jsonInputStream, String experimentId, Map<String, String> algAuthors
  ) throws IOException {
    // Load the results
    List<AlgorithmResult> results = loadJson(jsonInputStream, algAuthors);
    // Get the problems
    List<String> problems = getProblemsNames(results);
    // Sort the results
    sortResults(results);
    // Return the SVG string
    return createSvgString(experimentId, results, problems);
  }
}

