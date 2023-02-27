# SEAGE

SEAGE is a framework for metaheuristic collaboration. See more at https://www.seage.org

## Build and run

Simply run the build script:
```
./scripts/build.sh
```

Then run the launcher which will provide you some help:
```
./scripts/run.sh
```

To see what is implemented run:
```
./scripts/run.sh info
```

To see what is implemented in more details for a specified problem run:
```
./scripts/run.sh details -p SAT
```

## Run experiments

Traveling Salesman Problem (TSP) solved by the Genetic Algorithm:
```
./scripts/run.sh experiment-approach -a GeneticAlgorithm -i TSP:berlin52 -t 10 -n 10
```

Satisfiability Problem (SAT) solved by the Tabu Search
```
./scripts/run.sh experiment-approach -a TabuSearch -i SAT:uf20-01 -t 10 -n 10
```
### Inspect results

#### H2 database
When running with no database uri specified, the results are stored in the local H2 database. You can inspect the results as follows:
```
java -jar seage-launcher/build/install/seage-launcher/lib/h2-*.jar
```
Provide the following information:
- `JDBC URL`:`jdbc:h2:<path-to-seage>/output/seage.local.h2`
- `username`: `sa`
- `password`: empty

## Running against the PostgreSQL server
```
DB_USER=<username> \
DB_PASSWORD=<password> \
DB_URL="jdbc:postgresql://<server>/seage" \
./scripts/run.sh experiment-single-random -a TabuSearch -i SAT:uf20-01 -t 10 -n 10
```

## Development with Docker support
You can run SEAGE also from source against the database running in docker.
There are three containers:
- seage (you don't need to run it)
- database (postgresql, `localhost:25432`)
- pgadmin (http://localhost:28080)

### Start containers:
```
docker compose up database pgadmin
```

### Run experiments
```
DB_URL="jdbc:postgresql://localhost:25432/seage" ./scripts/run.sh experiment-single-random -a TabuSearch -i SAT:uf20-01 -t 10 -n 10
```

### Inspect results
The results can be inspected using `pgAdmin` at the address http://localhost:28080
- username: `seage@seage.org`
- password: `seage`
- database password: `seage`