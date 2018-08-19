package org.seage.experimenter.runner;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.seage.data.DataNode;
import org.seage.experimenter.ExperimentTask;

public class SparkExperimentTasksRunner implements IExperimentTasksRunner {

    @Override
    public void performExperimentTasks(List<ExperimentTask> tasks) {        
        SparkConf conf = new SparkConf().setAppName("SEAGE test app2");
        //if(args.length > 0 && args[0].equals("debug"))
            //conf.setMaster("local[8]");
  
        try(JavaSparkContext sc = new JavaSparkContext(conf)) {
        
                JavaRDD<ExperimentTask> distTasks = sc.parallelize(tasks);
                
                List<DataNode> dn = distTasks.map(t -> {
                    t.run();
                    return t.getExperimentTaskReport();
                }).collect();
//            List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//            JavaRDD<Integer> distData = sc.parallelize(data);
//            long c = distData.filter(t -> {                
//                return true;
//            }).count();
        }
    }

}
