package org.seage.experimenter.runner;

import java.util.List;
import java.util.stream.Collectors;

import org.seage.experimenter.ExperimentTask;

public class SparkExperimentTasksRunner implements IExperimentTasksRunner {

    @Override
    public void performExperimentTasks(List<ExperimentTask> tasks) {
        List<Integer> dn = tasks.parallelStream().map(t -> {            
            try {
                t.run();                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return 1;
        }).collect(Collectors.toList());

    }

}
