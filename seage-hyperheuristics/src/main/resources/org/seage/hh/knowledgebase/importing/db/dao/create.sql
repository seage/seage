--DROP TABLE IF EXISTS fileInfos ;
--DROP TABLE IF EXISTS scanInfos ;

CREATE TABLE IF NOT EXISTS scanInfos (
	id SERIAL PRIMARY KEY,
	hostname VARCHAR(150),
	rootPath VARCHAR(500),
	scanDate TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fileInfos (
	id SERIAL PRIMARY KEY,
	scanId INTEGER NOT NULL REFERENCES scanInfos (id),
	hashValue VARCHAR(50),	
	dirName VARCHAR(200),
	fileName VARCHAR(200),
	fileExt VARCHAR(50),
	filePath VARCHAR(500),
	size BIGINT,
	created TIMESTAMP,
	modified TIMESTAMP,
	owner VARCHAR(50),
	archived BOOLEAN
);