-- DROP TABLE IF EXISTS experiments;
-- DROP TABLE IF EXISTS experiment_tasks;
CREATE SCHEMA IF NOT EXISTS seage;
-- GRANT ALL ON SCHEMA seage TO SA;
CREATE TABLE IF NOT EXISTS seage.experiments (
	experiment_id UUID NOT NULL PRIMARY KEY,
	experiment_type VARCHAR(100) NOT NULL,
	problem_id VARCHAR(100) NOT NULL,
	instance_id VARCHAR(100) NOT NULL,
	algorithm_id VARCHAR(100) NOT NULL,
	config TEXT,
	start_date TIMESTAMP,
	end_date TIMESTAMP,
	hostname VARCHAR(100),
	score DOUBLE PRECISION
);
CREATE TABLE IF NOT EXISTS seage.experiment_tasks (
	experiment_task_id UUID NOT NULL PRIMARY KEY,
	experiment_id UUID NOT NULL REFERENCES seage.experiments (experiment_id) ON DELETE CASCADE,
	job_id INTEGER NOT NULL,
	stage_id INTEGER NOT NULL,
	problem_id VARCHAR(100) NOT NULL,
	instance_id VARCHAR(100) NOT NULL,
	algorithm_id VARCHAR(100) NOT NULL,
	config_id VARCHAR(100) NOT NULL,
	start_date TIMESTAMP,
	end_date TIMESTAMP,
	score DOUBLE PRECISION,
	config TEXT,
	statistics TEXT
);
CREATE TABLE IF NOT EXISTS seage.solutions (
	solution_id PRIMARY KEY,
	experiment_task_id UUID REFERENCES seage.experiment_tasks (experiment_task_id) ON DELETE CASCADE,
	hash VARCHAR(1000),
	solution TEXT,
	objective_value DOUBLE PRECISION,
	iteration_number BIGINT
);

CREATE OR REPLACE VIEW seage.experiments_overview AS
SELECT e.experiment_id,
	min(e.problem_id) AS problem_id,
	min(e.algorithm_id) AS algorithm_id,
	min(e.instance_id) AS instance_id,
	min(e.config) AS config,
	count(DISTINCT et.id) AS experiment_tasks_count,
	count(s.id) AS solutions_count,
	min(e.score) AS score
FROM seage.solutions s
	LEFT JOIN seage.experiment_tasks et ON s.experiment_task_id = et.experiment_task_id
	LEFT JOIN seage.experiments e ON e.experiment_id = et.experiment_id
GROUP BY e.experiment_id
ORDER BY e.start_date;