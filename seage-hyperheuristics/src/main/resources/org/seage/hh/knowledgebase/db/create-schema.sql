-- DROP TABLE IF EXISTS experiments;
-- DROP TABLE IF EXISTS experiment_tasks;

-- CREATE TABLE IF NOT EXISTS experiments (
-- 	id SERIAL PRIMARY KEY,
-- 	hostname VARCHAR(150),
-- 	experimentType VARCHAR(100),
-- 	experimentDate TIMESTAMP
-- );

CREATE SCHEMA IF NOT EXISTS seage;
-- GRANT ALL ON SCHEMA seage TO SA;

CREATE TABLE IF NOT EXISTS seage.experiments (
	id SERIAL PRIMARY KEY,
	experiment_type VARCHAR(100) NOT NULL,
	experiment_id VARCHAR(100) NOT NULL UNIQUE,	
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
	id SERIAL PRIMARY KEY,
	experiment_task_id VARCHAR(100) NOT NULL UNIQUE,
	experiment_id VARCHAR(100) NOT NULL REFERENCES seage.experiments (experiment_id),
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
	-- solutions_initial TEXT,
	-- solutions_log TEXT,
	-- solutions_final TEXT,
	statistics TEXT
);

-- CREATE TABLE IF NOT EXISTS seage.solutions (
-- 	id SERIAL PRIMARY KEY,
-- 	experiment_task_id VARCHAR(100) REFERENCES seage.experiment_tasks (experiment_task_id),
-- 	solution TEXT,
-- 	objective_value DOUBLE PRECISION,
-- 	iteration_number BIGINT
-- );
