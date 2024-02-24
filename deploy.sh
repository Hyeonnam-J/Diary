#!/usr/bin/env bash

port_in_use() {
    sudo lsof -ti :$1
}

if port_in_use 8081; then
    IDLE_PROFILE=set2
else
    IDLE_PROFILE=set1
fi

echo ">>> 전환할 profile: $IDLE_PROFILE"

if [ ${IDLE_PROFILE} == set1 ]
then
    IDLE_PORT="8081"
else
    IDLE_PORT="8082"
fi

echo ">>> 전환할 port: $IDLE_PORT"

echo ">>> 새 어플리케이션 배포"

REPOSITORY=/home/ec2-user/deploy
cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)

echo ">>> $JAR_NAME 실행"
echo ">>> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행."

nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

sleep 10

echo ">>> 전환한 port로 nginx 설정 변경"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
echo ">>> restart reload"
sudo service nginx reload

if [ "$IDLE_PORT" == "8081" ]; then
    KILL_TO_PORT="8082"
elif [ "$IDLE_PORT" == "8082" ]; then
    KILL_TO_PORT="8081"
fi

echo ">>> 프로세스 종료할 이전 버전의 port: $KILL_TO_PORT"
echo ">>> $KILL_TO_PORT PID 확인"

if port_in_use $KILL_TO_PORT; then
    KILL_PID=$(sudo lsof -ti tcp:${KILL_TO_PORT})
    echo ">>> kill -15 $KILL_PID"
    kill -15 ${KILL_PID}
    sleep 5
else
    echo ">>> 이전 버전의 프로세스가 없습니다."
fi