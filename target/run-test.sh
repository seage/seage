 cd `dirname $0`
LAUNCHER="`ls bin/seage-launcher*.jar`"
java -jar -Xms1024m $LAUNCHER $@

