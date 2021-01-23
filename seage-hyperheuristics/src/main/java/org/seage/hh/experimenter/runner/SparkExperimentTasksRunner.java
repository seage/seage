package org.seage.hh.experimenter.runner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipOutputStream;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.hh.experimenter.ExperimentTask;

public class SparkExperimentTasksRunner implements IExperimentTasksRunner {

  @Override
  public List<DataNode> performExperimentTasks(List<ExperimentTask> tasks, Function<ExperimentTask, Void> reportFn) {
    SparkConf conf = new SparkConf().setAppName("SEAGE test app2");
    conf.setMaster("local[8]");

    final int batchSize = 128;
    final int partSize = 32;

    List<DataNode> result = new ArrayList<>();

    try (JavaSparkContext sc = new JavaSparkContext(conf)) {

      for (int i = 0; i < (tasks.size() / batchSize) + 1; i++) {
        int ix0 = i * batchSize;
        int ix1 = Math.min(ix0 + batchSize, tasks.size());

        List<ExperimentTask> taskBatch = tasks.subList(ix0, ix1);

        JavaRDD<ExperimentTask> distTasks = sc.parallelize(taskBatch, partSize);

        result.addAll(distTasks.map(t -> {
          t.run();
          return t.getExperimentTaskReport();
        }).collect());

        // try (ZipOutputStream zos = new ZipOutputStream(
        //     new FileOutputStream(new File(reportPath + "-" + i)))) {
        //   for (ExperimentTask r : reports) {
        //     XmlHelper.writeXml(r.getExperimentTaskReport(), zos, r.getReportName());
        //   }
        // } catch (Exception e) {
        //   // TODO Auto-generated catch block
        //   e.printStackTrace();
        // }
      }
    }
    return result;
  }

}
