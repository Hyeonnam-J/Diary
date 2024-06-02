##!/usr/bin/env bash
#
## 포트가 사용 중인지 확인
#port_in_use() {
#    sudo lsof -ti :$1
#}
#
## 쉬고 있는 profile 할당
#if port_in_use 8081; then
#    IDLE_PROFILE=set2
#else
#    IDLE_PROFILE=set1
#fi
#
#echo ">>> 전환할 profile: $IDLE_PROFILE"
#
## 쉬고 있는 port
#if [ ${IDLE_PROFILE} == set1 ]
#then
#    IDLE_PORT="8081"
#else
#    IDLE_PORT="8082"
#fi
#
#echo ">>> 전환할 port: $IDLE_PORT"
#
#echo ">>> 새 어플리케이션 배포"
#
#REPOSITORY=/home/ec2-user/deploy
#cd $REPOSITORY
#JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)
#
#echo ">>> $JAR_NAME profile=$IDLE_PROFILE 실행."
#
#nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
#
## 새로운 포트가 열릴 때까지 대기
#WAIT_TIME=0
#PORT_OPEN=false
#while [ $WAIT_TIME -lt 20 ]; do
#    if port_in_use $IDLE_PORT; then
#        PORT_OPEN=true
#        break
#    fi
#    sleep 3
#    WAIT_TIME=$((WAIT_TIME + 3))
#done
#
#if ! $PORT_OPEN; then
#    echo ">>> 새로운 포트가 열리지 않았습니다. 스크립트를 종료합니다."
#    exit 1
#fi
#
#echo ">>> 전환한 port로 nginx 설정 변경"
#echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
#echo ">>> nginx reload"
#sudo service nginx reload
#
## 종료할 포트 설정
#if [ "$IDLE_PORT" == "8081" ]; then
#    KILL_TO_PORT="8082"
#elif [ "$IDLE_PORT" == "8082" ]; then
#    KILL_TO_PORT="8081"
#fi
#
#echo ">>> 프로세스 종료할 이전 버전의 port: $KILL_TO_PORT"
#
## kill port with pid
#if port_in_use $KILL_TO_PORT; then
#    echo ">>> $KILL_TO_PORT PID 확인"
#    KILL_PID=$(sudo lsof -ti tcp:${KILL_TO_PORT})
#    echo ">>> kill -15 $KILL_PID"
#    kill -15 ${KILL_PID}
#    sleep 5
#else
#    echo ">>> 이전 버전의 프로세스가 없습니다."
#fi