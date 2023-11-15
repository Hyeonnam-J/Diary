package com.hn.api.diary.util;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwsKey {

    // todo: 외부 저장소를 사용하여 키를 관리하는 것이 효율적.
    private static volatile SecretKey key;

    public static SecretKey getJwsSecretKey(){
        if(key == null){
            synchronized (JwsKey.class){
                if(key == null){
                    key = Jwts.SIG.HS256.key().build();
                }
            }
        }
        return key;
    }
}
