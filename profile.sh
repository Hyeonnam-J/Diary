#!/usr/bin/env bash

function find_current_profile()
{
#    RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
#
#    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
#    then
#        CURRENT_PROFILE=set2
#    else
#        CURRENT_PROFILE=$(sudo curl -s http://localhost/profile)
#    fi
#
#    if [ ${CURRENT_PROFILE} == set1 ]
#    then
#        IDLE_PROFILE=set2 # Nginx와 연결되지 않은 profile
#    else
#        IDLE_PROFILE=set1
#    fi
#
#    # bash script는 return 기능이 없기 떄문에,
#    # echo를 통해서 출력하면 이 값을 클라이언트가 사용할 수 있습니다.
#    echo "${IDLE_PROFILE}"

    echo "profile.sh-find_current_profile"

    CURRENT_PORT=$(sudo netstat -tnlp | grep LISTEN | awk '{print $4}' | awk -F: '{print $2}' | sort -n | uniq)

    if [ ${CURRENT_PORT} -eq 8081 ]
    then
        CURRENT_PROFILE=set1
    else
        CURRENT_PROFILE=set2
    fi

    echo "${CURRENT_PROFILE}"
}

function find_current_port()
{
    echo "profile.sh-find_current_port"

    CURRENT_PROFILE=$(find_current_profile)

    if [ ${CURRENT_PROFILE} == set1 ]
    then
        echo "8081"
    else
        echo "8082"
    fi
}