cd `dirname $0`
git pull origin master

mvn -f ../pom.xml clean package -Dmaven.test.failure.ignore=true
