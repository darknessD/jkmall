package com.jkmall.order.controller;

import com.alibaba.fastjson.JSON;
import com.jchen.entity.Result;
import com.jchen.entity.StatusCode;
import com.jkmall.order.pojo.OrderItem;
import com.jkmall.order.service.CartService;
import com.jkmall.order.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /***
     * 加入购物车
     * @param num:购买的数量
     * @param id：购买的商品(SKU)ID
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(Integer num, Long id){
        //用户名
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String claims = JwtUtil.decode(details.getTokenValue());
        Map<String, Object> map = JSON.parseObject(claims, Map.class);
        //将商品加入购物车
        cartService.add(num,id, (String) map.get("username"));
        return new Result(true, StatusCode.OK,"加入购物车成功！");
    }

    /***
     * 查询用户购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result list(){
        //用户名
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String claims = JwtUtil.decode(details.getTokenValue());
        Map<String, Object> map = JSON.parseObject(claims, Map.class);
        List<OrderItem> orderItems = cartService.list((String) map.get("username"));
        return new Result(true,StatusCode.OK,"购物车列表查询成功！",orderItems);
    }
}
