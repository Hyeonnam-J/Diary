#!/bin/bash

REPOSITORY=/home/ec2-user/deploy
cd $REPOSITORY 
# cd deploy

JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1) 
# JAR_NAME=$(ls | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

APP_NAME=diary-api
CURRENT_PID=$(pgrep -fl diary-api | grep java | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
    echo "NOT RUNNING"
else
    echo "> kill -9 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

# echo "> $JAR_PATH 배포"
echo "> 실행"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &