cd `dirname $0`
LAUNCHER="`ls bin/seage-launcher*.jar`"
java -jar -Xms1024m $LAUNCHER $@ 2> seage.err 1>seage.log
