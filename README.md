Simply,
- run `sh target/build.sh` to build the project 
- then run `sh target/run-test.sh` to run launcher which will provide you some help.

To see what is implemented
- run `sh target/run-test.sh list`

Experiment examples:
- `sh target/run-test.sh experiment-single-random -p TSP -a GeneticAlgorithm -i berlin52 -t 10 -n 10`
- `sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10`

Running against the database
```
DB_USER=<username> \
DB_PASSWORD=<password> \
DB_URL="jdbc:postgresql://<server>/seage" \
sh target/run-test.sh experiment-single-random -p SAT -a TabuSearch -i uf20-01 -t 10 -n 10
```