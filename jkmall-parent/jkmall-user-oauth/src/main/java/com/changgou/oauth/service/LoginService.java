package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    public AuthToken login(String username, String password, String clientId, String clientSecret){
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        String url = serviceInstance.getUri().toString()+"/oauth/token";

        //prepare entity
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", "Basic "+new String(Base64.getEncoder().encode((clientId+":"+clientSecret).getBytes())));
        HttpEntity httpEntity = new HttpEntity(body, header);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String, String> result = exchange.getBody();
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(result.get("access_token"));
        authToken.setRefreshToken(result.get("refresh_token"));
        authToken.setJti(result.get("jti"));
        return authToken;
    }
}
