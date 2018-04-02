#!/bin/sh

APP_BASE=/usr/apps/pms
DEPLOYMENTS=$APP_BASE/deployments
LOGS=$APP_BASE/logs
SCRIPTS=$APP_BASE/scripts

APP_WAR=$DEPLOYMENTS/pms.war

TODAY=`date +%Y-%m-%d`
NOW=$(date +"%T")

LOG_FILE=$LOGS/"start-$TODAY.log"

#echo $LOG_FILE
echo "Starting application at $NOW" >> $LOG_FILE

nohup java -Xms1024m -Xmx2048m -jar $APP_WAR >> $LOG_FILE &

exit 0