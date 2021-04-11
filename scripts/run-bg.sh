cd `dirname $0`/..

set -e

if [ "x$DB_URL" = "x" ]; then
  export DB_URL="jdbc:postgresql://localhost/seage"
fi

./scripts/run.sh $@ &> /dev/null & disown
