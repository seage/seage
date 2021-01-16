DROP TABLE IF EXISTS experiments ;
DROP TABLE IF EXISTS experimentTasks ;

CREATE TABLE IF NOT EXISTS experiments (
	id SERIAL PRIMARY KEY,
	hostname VARCHAR(150),
	experimentType VARCHAR(100),
	experimentDate TIMESTAMP
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