cd `dirname $0`
LAUNCHER="`ls bin/seage-launcher*.jar`"
 
#List of used problems and algorithms
problemsIDs="TSP SAT"
algorithmIDs="GeneticAlgorithm TabuSearch AntColony SimulatedAnnealing"

#Use each algorithm on each problem
for problemID in ${problemsIDs};
do
    for algorithmID in ${algorithmIDs};
    do
        java -jar -Xms1024m -Dlog4j.configurationFile=log4j2.xml $LAUNCHER $@ -p $problemID -a $algorithmID -i -
    done
done

