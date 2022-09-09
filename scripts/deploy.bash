
echo "DEPLOYING"
DIR="/home/doms/Desktop/Development/wildfly-26.1.1.Final/standalone/deployments"
mvn clean install
cp ear/target/ear-1.0.ear $DIR