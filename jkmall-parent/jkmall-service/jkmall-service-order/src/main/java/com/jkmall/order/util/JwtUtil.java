package com.jkmall.order.util;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

public class JwtUtil {

    public static String decode(String token){
        Jwt jwt = JwtHelper.decode(token);
        return jwt.getClaims();
    }
}
