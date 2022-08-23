cd `dirname $0`/..

set -e

if [ "x$DB_URL" = "x" ]; then
  export DB_URL="jdbc:postgresql://localhost:25432/seage"
fi

# Run SEAGE
./scripts/run.sh $@ &> /dev/null & disown
#
echo "SEAGE starting..."
sleep 1
# Print the log file path
LOG_DIR="`pwd`/logs"
LOG_FILE="`find $LOG_DIR -type f -name 'seage-*'| sort -r | head -1`"

echo "See the log file for details:"
echo "$LOG_FILE"
