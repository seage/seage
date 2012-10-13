cd `dirname $0`
LAUNCHER="`ls bin/seage-launcher*.jar`"
java -jar -Xms1024m $LAUNCHER $@ 2> /dev/null 1>seage.out&
echo "SEAGE is running... See seage.log for details."
