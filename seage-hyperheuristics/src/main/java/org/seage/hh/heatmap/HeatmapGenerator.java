/**
 * Program generates a heatmap, visualizating given data from experiment
 * 
 * @author David Omrai
 */

package org.seage.hh.heatmap;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
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
import java.util.Collections;
import java.util.HashMap;
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
  public class ExperimentScoreCards {
    public List<ExperimentScoreCard> results;

    public ExperimentScoreCards() {
      this.results = new ArrayList<>();
    }
  }

  public static class HeatmapForTagCreator {
    private HeatmapForTagCreator() {
      // Empty constructor
    }

    public static void createHeatmapForTag(String tag) throws Exception {
      ExperimentReporter reporter = new ExperimentReporter();
    
      List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);

      HashMap<String, ExperimentScoreCard> algExperiment = new HashMap<>();

      Gson gson = new Gson();
      Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

      for (ExperimentRecord experiment : experiments) {
        ExperimentScoreCard expScoreCard = gson.fromJson(experiment.getScoreCard(), objType);

        if (expScoreCard == null) {
          continue;
        }

        if (algExperiment.containsKey(experiment.getAlgorithmID())) {
          ExperimentScoreCard bestExpScoreCard = algExperiment.get(experiment.getAlgorithmID());
          double bestOverScore = bestExpScoreCard.getTotalScore();

          for (String problemID : expScoreCard.getProblems()) {
            System.out.println(problemID);
            if ( (!bestExpScoreCard.getProblems().contains(problemID)) || 
                expScoreCard.getProblemScore(problemID) > bestExpScoreCard.getProblemScore(problemID)) {
              System.out.println("Hey, I'm here");
              // there is the problem, it's not storing the problem id
              bestExpScoreCard.putProblemScore(problemID, expScoreCard.getProblemScore(problemID));              
            }
            bestOverScore += bestExpScoreCard.getProblemScore(problemID);
            System.out.println(bestOverScore);
          }
          bestExpScoreCard.setTotalScore(bestOverScore/(bestExpScoreCard.getProblems().size()));
          System.out.println(bestExpScoreCard.getProblems().size());
        } else {
          algExperiment.put(experiment.getAlgorithmID(), expScoreCard);
        }
      }

      // Generate the svg heatmap file
      HeatmapGenerator.createSvgFile(tag, 
        HeatmapGenerator.loadExperimentScoreCards(new ArrayList<>(algExperiment.values()), 
        new HashMap<>()), "./output" + "/" + tag + "-heatmap.svg");
      }
  }

  // Path where the metadata are stored
  static String svgTemplatePath = "/heatmap.svg.template";

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
  public HeatmapGenerator() {
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
    List<String> problems = results.isEmpty() ? new ArrayList<>()
        : new ArrayList<>(results.get(0).problemsResults.keySet());
    // Sort the problem names
    Collections.sort(problems);
    return problems;
  }

  /**
   * Method sorts the results list using the hhs overall scores.
   */
  protected  static void sortResults(List<AlgorithmResult> results) {
    // Sort the results by their overall score
    Collections.sort(results, (var lar, var rar) -> Double.compare(rar.score, lar.score));
  }

  /**
   * Method loads the xml file.
   * 
   * @param xmlPath path to the xml file
   */
  public static void loadXmlFile(
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

  private static AlgorithmResult getAlgorithmResult (ExperimentScoreCard scoreCard, Map<String, String> algAuthors) {
      AlgorithmResult result = new AlgorithmResult();

      // Store the result informations
      result.name = scoreCard.getName();
      result.score = Double.parseDouble(String.format("%.5f",
          scoreCard.getTotalScore()));
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

  public static List<ExperimentScoreCard> loadJson(
      String jsonString) {
    // Read the json string
    Gson gson = new Gson();
    Type listType = new TypeToken<ExperimentScoreCards>() {}.getType();
    ExperimentScoreCards experimentResults = gson.fromJson(jsonString, listType);
    
    return experimentResults.results;
  }

  public static List<AlgorithmResult> loadExperimentScoreCards(
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
   * @param experimentId id of the experiment
   * @throws IOException exception if the page couldn't be created
   */
  protected static String createSvgString(
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
    try (
        InputStream svgTemplateStream = HeatmapGenerator
            .class.getResourceAsStream(svgTemplatePath)
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
   * @param experimentId id of the experiment
   * @param results list of algorithm results (with colors)
   * @param svgFileDest svg file destination
   * @throws IOException exception if the page couldn't be created
   */
  public static void createSvgFile(
      String experimentId, List<AlgorithmResult> results, String svgFileDest
  ) throws IOException {
    // Get the problems
    List<String> problems = HeatmapGenerator.getProblemsNames(results);
    try (FileWriter fileWriter = new FileWriter(svgFileDest);) {
      fileWriter.write(createSvgString(experimentId, results, problems));
    }
  }

  /**
   * Method receives neccesary data and create the result svg file.
   * @param results list of algorithm results (with colors)
   * @param experimentId id of the competition experiment
   */
  public static String createHeatmap(
      List<ExperimentScoreCard> table, String experimentId, Map<String, String> algAuthors
  ) throws IOException {
    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(table, algAuthors);
    // Get the problems
    List<String> problems = HeatmapGenerator.getProblemsNames(results);
    // Sort the results
    HeatmapGenerator.sortResults(results);
    // Return the SVG string
    return HeatmapGenerator.createSvgString(experimentId, results, problems);
  }
}

