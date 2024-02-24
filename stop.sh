#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) # 현재 stop.sh가 속해있는 경로
source ${ABSDIR}/profile.sh # 해당 코드로 profile.sh 내의 함수 사용

IDLE_PORT=$(find_idle_port)

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

#if [ -z ${KILL_PID} ]
#then
#    echo ">>> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
#else
#    echo ">>> kill -15 $KILL_PID"
#    kill -15 ${KILL_PID}
#    sleep 5
#fi