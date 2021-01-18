-- DROP TABLE IF EXISTS experiments;
-- DROP TABLE IF EXISTS experiment_tasks;

-- CREATE TABLE IF NOT EXISTS experiments (
-- 	id SERIAL PRIMARY KEY,
-- 	hostname VARCHAR(150),
-- 	experimentType VARCHAR(100),
-- 	experimentDate TIMESTAMP
-- );
CREATE SCHEMA seage;
-- GRANT ALL ON SCHEMA seage TO SA;

CREATE TABLE IF NOT EXISTS seage.experiments (
	id SERIAL PRIMARY KEY,
	experiment_id VARCHAR(100),
	experiment_name VARCHAR(100),
	problem_id VARCHAR(100),
	instance_id VARCHAR(100),
	algorithm_id VARCHAR(100),
	config VARCHAR(100),
	start_date TIMESTAMP,
	duration DATETIME,	
	hostname VARCHAR(100)	
);

-- CREATE TABLE IF NOT EXISTS fileInfos (
-- 	id SERIAL PRIMARY KEY,
-- 	scanId INTEGER NOT NULL REFERENCES scanInfos (id),
-- 	hashValue VARCHAR(50),	
-- 	dirName VARCHAR(200),
-- 	fileName VARCHAR(200),
-- 	fileExt VARCHAR(50),
-- 	filePath VARCHAR(500),
-- 	size BIGINT,
-- 	created TIMESTAMP,
-- 	modified TIMESTAMP,
-- 	owner VARCHAR(50),
-- 	archived BOOLEAN
-- );