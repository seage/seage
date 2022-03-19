package org.seage.problem.fsp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.problem.jsp.ScheduleJobInfo;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.ScheduleOperationInfo;

public class FspJobsDefinition extends JspJobsDefinition {

  public FspJobsDefinition(ProblemInstanceInfo instanceInfo, InputStream jobsDefinitionStream)
      throws Exception {
    super(instanceInfo, jobsDefinitionStream);
  }

  @Override
  protected void createJobInfos(InputStream jobsDefinitionStream) throws Exception {
    List<List<Integer>> data = readInputData(jobsDefinitionStream);
    List<List<Integer>> jobs = transposeData(data);

    _numMachines = jobs.get(0).size();
    _jobInfos = new ScheduleJobInfo[jobs.size()];

    for(int i=0;i<jobs.size();i++) {
      int jobID = i+1;
      ScheduleOperationInfo[] operationInfos = new ScheduleOperationInfo[_numMachines];
      for(int j=0;j<_numMachines;j++) {
        ScheduleOperationInfo oper = new ScheduleOperationInfo();
        oper.OperationID = j+1;
        oper.JobID = jobID;
        oper.MachineID = j+1;
        oper.Length = jobs.get(i).get(j);
        operationInfos[j] = oper;
      }
      _jobInfos[i] = new ScheduleJobInfo(jobID, operationInfos);
    }
  }

  private List<List<Integer>> readInputData(InputStream inputDataStream) throws Exception {
    List<List<Integer>> result = new ArrayList<>();
    try(Scanner sc = new Scanner(inputDataStream)){
      int lineNumber = 0;
      int numbersPerLine = 0;
      while(sc.hasNextLine()) {
        lineNumber++;
        String line = sc.nextLine();
        List<Integer> lineInts = new ArrayList<>();
        try(Scanner scLine = new Scanner(line)) {
          while(scLine.hasNextInt()) {
            int value = scLine.nextInt();
            lineInts.add(value);
          }
        }
        if(numbersPerLine == 0) {
          numbersPerLine = lineInts.size();
        }
        if(numbersPerLine != lineInts.size()) {
          throw new Exception(String.format("Invalid input data. Incorrect line length: %d, line: %d", lineInts.size(), lineNumber));
        }
        result.add(lineInts);
      }
    }
    return result;
  }

  @Override
  public int getSize() {
    return getJobsCount();
  }

  private List<List<Integer>> transposeData(List<List<Integer>> data) {
    List<List<Integer>> result = new ArrayList<>();

    int m = data.get(0).size();
    int n = data.size();

    for(int i = 0; i < m; i++) {
      List<Integer> line = new ArrayList<>();
      for(int j = 0; j < n; j++) {
        line.add(data.get(j).get(i));
      }
      result.add(line);
    }
    return result;
  }

}
