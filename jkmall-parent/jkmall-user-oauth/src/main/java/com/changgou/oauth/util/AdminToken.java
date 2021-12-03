package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class AdminToken {
    public static String adminToken(){
        ClassPathResource resource = new ClassPathResource("jkmall.jks");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, "jkmall".toCharArray());
        //获取秘钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jkmall", "jkmall".toCharArray());
        //获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> payload = new HashMap<>();
        payload.put("nickname", "jeremy");
        payload.put("address", "roho");
        payload.put("authorities", new String[] {"admin"});

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));
        String token = jwt.getEncoded();
        return token;


    }
}
