#!/bin/sh

APP_BASE=/usr/apps/pms
DEPLOYMENTS=$APP_BASE/deployments
LOGS=$APP_BASE/logs
SCRIPTS=$APP_BASE/scripts

APP_WAR=$DEPLOYMENTS/pms.war

TODAY=`date +%Y-%m-%d`
NOW=$(date +"%T")

LOG_FILE=$LOGS/"stop-$TODAY.log"

#echo $LOG_FILE
echo "Stopping application at $date" >> $LOG_FILE

# ps -ef | grep /usr/apps/pms/deployments/pms.war | grep -v grep| awk '{print $2}'
kill `ps -ef | grep $APP_WAR | grep -v grep| awk '{print $2}'`

echo "Stopped application at $date" >> $LOG_FILE

exit 0