package org.seage.hh.experimenter.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.seage.data.xml.XmlHelper;
import org.seage.hh.experimenter.ExperimentTask;

public class SparkExperimentTasksRunner implements IExperimentTasksRunner {

    @Override
    public void performExperimentTasks(List<ExperimentTask> tasks, String reportPath) {
        SparkConf conf = new SparkConf().setAppName("SEAGE test app2");
        //if(args.length > 0 && args[0].equals("debug"))
        //conf.setMaster("local[8]");

        final int batchSize = 128;
        final int partSize = 32;

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {

            for (int i = 0; i < (tasks.size() / batchSize) + 1; i++) {
                int ix0 = i * batchSize;
                int ix1 = Math.min(ix0 + batchSize, tasks.size());

                List<ExperimentTask> taskBatch = tasks.subList(ix0, ix1);

                JavaRDD<ExperimentTask> distTasks = sc.parallelize(taskBatch, partSize);

                List<ExperimentTask> reports = distTasks.map(t -> {
                    t.run();
                    return t;
                }).collect();

                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath + "-" + i)))) {
                    for (ExperimentTask r : reports) {
                        XmlHelper.writeXml(r.getExperimentTaskReport(), zos, r.getReportName());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//            JavaRDD<Integer> distData = sc.parallelize(data);
//            long c = distData.filter(t -> {                
//                return true;
//            }).count();
        }
    }

}
