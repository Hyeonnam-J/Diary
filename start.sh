#!/usr/bin/env bash

# 배포할 신규 버전 프로젝트를 stop.sh로 종료한 profile로 실행
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh # 해당 코드로 profile.sh 내의 함수 사용

REPOSITORY=/home/ec2-user/deploy
cd $REPOSITORY

echo ">>> 새 어플리케이션 배포"
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)

echo ">>> $JAR_NAME 실행"
IDLE_PROFILE=$(find_idle_profile)

# 위에서 보았던 것처럼 $IDLE_PROFILE에는 set1 or set2가 반환되는데
# 반환되는 properties를 실행한다는 뜻.
echo ">>> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

## IDLE_PROFILE 값에 따라 포트 번호 설정
#if [ "$IDLE_PROFILE" == "set1" ]; then
#    PORT_TO_KILL=8082
#else
#    PORT_TO_KILL=8081
#fi
#
## 해당 포트를 사용하는 프로세스의 PID 찾기
#PID=$(sudo lsof -t -i:$PORT_TO_KILL)
#
## PID가 존재하는 경우에만 프로세스 종료
#if [ -n "$PID" ]; then
#    echo ">>> 포트 $PORT_TO_KILL를 사용하는 프로세스 PID: $PID"
#    echo ">>> 프로세스를 종료합니다."
#    sudo kill $PID
#else
#    echo ">>> 포트 $PORT_TO_KILL를 사용하는 프로세스가 존재하지 않습니다."
#fi