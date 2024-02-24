#!/usr/bin/env bash

if sudo lsof -ti :8081; then
    IDLE_PROFILE=set2
else
    IDLE_PROFILE=set1
fi

if [ ${IDLE_PROFILE} == set1 ]
then
    IDLE_PORT="8081"
else
    IDLE_PORT="8082"
fi

echo ">>> 전환할 Port: $IDLE_PORT"
echo ">>> Port 전환"
# 아래 줄은 echo를 통해서 나온 결과를 | 파이프라인을 통해서 service-url.inc에 덮어쓸 수 있습니다.
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
echo ">>> restart Nginx"
sudo service nginx restart

if [ "$IDLE_PORT" == "8081" ]; then
    KILL_TO_PORT="8082"
elif [ "$IDLE_PORT" == "8082" ]; then
    KILL_TO_PORT="8081"
fi

echo ">>> $KILL_TO_PORT에서 구동 중인 애플리케이션 PID 확인"
KILL_PID=$(sudo lsof -ti tcp:${KILL_TO_PORT})

echo ">>> kill -15 $KILL_PID"
kill -15 ${KILL_PID}
sleep 5

REPOSITORY=/home/ec2-user/deploy
cd $REPOSITORY

echo ">>> 새 어플리케이션 배포"
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)

echo ">>> $JAR_NAME 실행"
echo ">>> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &