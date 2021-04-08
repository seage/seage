package org.seage.hh.knowledgebase.db.mapper;

import java.util.UUID;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.seage.hh.experimenter.Solution;

public interface SolutionMapper {

  class SolutionSqlProvider implements ProviderMethodResolver {
    private SolutionSqlProvider() {}

    public static String insertSolution() {
      return new SQL()
        .INSERT_INTO("seage.solutions")
        .VALUES("solution_id", "#{solutionID}::uuid")
        .VALUES("experiment_task_id", "#{experimentTaskID}::uuid")
        .VALUES("hash", "#{hash}")
        .VALUES("solution", "#{solution}")
        .VALUES("objective_value", "#{objectiveValue}")
        .VALUES("score", "#{score}")
        .VALUES("iteration_number", "#{iterationNumber}")
        .VALUES("date", "#{date}")
        .toString();
    }
  }
  
  @InsertProvider(type = SolutionSqlProvider.class, method = "insertSolution")
  int insertSolution(Solution solution);

  @Select("SELECT * FROM seage.solutions WHERE solution_id = #{solutionID}")
  @Results(id = "solutionResult", value = {
      @Result(property = "solutionID", column = "solution_id", id = true),
      @Result(property = "experimentTaskID", column = "experiment_task_id"),
      @Result(property = "hash", column = "hash"),
      @Result(property = "solution", column = "solution"),
      @Result(property = "objectiveValue", column = "objective_value"),
      @Result(property = "score", column = "score"),
      @Result(property = "iterationNumber", column = "iteration_number"),
      @Result(property = "date", column = "date"),
  })
  Solution getSolution(UUID solutionID);
}
