cd `dirname $0`
git pull origin master

mvn -f ../pom.xml clean test
