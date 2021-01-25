# SEAGE

SEAGE is a framework for metaheuristic collaboration. See more at http://www.seage.org

## Build

Simply run the build script:
```
sh target/build.sh
```

Then run the launcher which will provide you some help:
```
sh target/run-test.sh
```

To see what is implemented run:
```
sh target/run-test.sh list
```

## Run experiments

Traveling Salesman Problem (TSP) solved by the Genetic Algorithm:
```
sh target/run-test.sh experiment-single-random -p TSP -a GeneticAlgorithm -i berlin52 -t 10 -n 10
```

Satisfiability Problem (SAT) solved by the Tabu Search
```
sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10
```
## Inspect results

When running with no database uri specified, the results are stored in the local H2 database. You can inspect the results as follows:
```
java -jar target/bin/lib/h2-*.jar
```

Provide the following information:
- `JDBC URL`:
```
jdbc:h2:<path-to-seage>/target/output/seage.local.h2
```
- `username`: `sa`
- `password`: empty


## Running against the database server
```
DB_USER=<username> \
DB_PASSWORD=<password> \
DB_URL="jdbc:postgresql://<server>/seage" \
sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10
```

## Running in Docker
There are three containers:
- seage
- postgresql
- pgadmin (http://localhost:8090)

You can run them all using `docker-compose`
```
docker-compose up
```

Once all the containers are running you can run a new experiment as follows:
```
docker exec seage_seage_1 sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10
```

The results can be inspected using `pgAdmin` at the address http://localhost:8090
- username: `seage@seage.org`
- password: `seage`
- database password: `seage`

## Development
You can run SEAGE also from source against the database running in docker.

### Start containers:
```
docker-compose up
```

### Run experiments
```
DB_URL="jdbc:postgresql://localhost/seage" sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10
```

### Inspect results
Navigate to http://localhost:8090