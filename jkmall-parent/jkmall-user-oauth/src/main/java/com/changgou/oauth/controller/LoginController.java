package com.changgou.oauth.controller;


import com.changgou.oauth.service.LoginService;
import com.changgou.oauth.util.AuthToken;
import com.jchen.entity.Result;
import com.jchen.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class LoginController {

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public Result login(String username, String password){
        AuthToken authToken = loginService.login(username, password, clientId, clientSecret);
        return new Result(true, StatusCode.OK, "login success", authToken);
    }
}
