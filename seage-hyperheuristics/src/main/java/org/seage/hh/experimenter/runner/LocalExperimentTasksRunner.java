package org.seage.hh.experimenter.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import org.seage.data.xml.XmlHelper;
import org.seage.hh.experimenter.ExperimentTask;

public class LocalExperimentTasksRunner implements IExperimentTasksRunner {

  @Override
  public void performExperimentTasks(List<ExperimentTask> tasks, String reportPath) {

    List<Integer> dn = tasks.parallelStream().map(t -> {
      try {
        t.run();
        try (
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)))) {
          XmlHelper.writeXml(t.getExperimentTaskReport(), zos, t.getReportName());
        } catch (Exception e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return 1;
    }).collect(Collectors.toList());

  }

}
