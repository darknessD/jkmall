package com.jkmall.pay.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.jchen.entity.Result;
import com.jchen.entity.StatusCode;
import com.jkmall.pay.service.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/pay")
public class WechatPayController {
    
    @Autowired
    private WechatPayService wechatPayService;
    
    @RequestMapping("/create/native")
    public Result createPay(String orderNo, String totalAmount){
        Map wechatPay = wechatPayService.createWechatPay(orderNo, totalAmount);
        return new Result(true, StatusCode.OK, "Success", wechatPay);
    }

    @RequestMapping("/query/status")
    public Result queryPayStatus(String orderNo){
        Map map = wechatPayService.queryWechatpayStatus(orderNo);
        return new Result(true, StatusCode.OK, "Success", map);
    }

    /***
     * 支付回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request){
        InputStream inStream;
        try {
            //读取支付回调数据
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 将支付回调数据转换成xml字符串
            String result = new String(outSteam.toByteArray(), "utf-8");
            //将xml字符串转换成Map结构
            Map<String, String> map = WXPayUtil.xmlToMap(result);

            //响应数据设置
            Map respMap = new HashMap();
            respMap.put("return_code","SUCCESS");
            respMap.put("return_msg","OK");
            return WXPayUtil.mapToXml(respMap);
        } catch (Exception e) {
            e.printStackTrace();
            //记录错误日志
        }
        return null;
    }
}
