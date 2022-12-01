cd `dirname $0`/..

LOG_DIR="`pwd`/logs"
LOG_FILE="`find $LOG_DIR -type f -name 'seage-*'| sort -r | head -1`"

cat $LOG_FILE