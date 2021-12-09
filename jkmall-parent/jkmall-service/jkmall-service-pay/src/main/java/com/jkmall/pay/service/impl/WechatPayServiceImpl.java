package com.jkmall.pay.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.jchen.entity.HttpClient;
import com.jkmall.pay.service.WechatPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.partner}")
    private String partner;

    @Value("${wechat.partnerkey}")
    private String partnerkey;

    @Value("${wechat.notifyurl}")
    private String notifyurl;

    @Override
    public Map createWechatPay(String orderNo, String totalAmount) {
        try {
            //1、封装参数
            Map param = new HashMap();
            param.put("appid", appid);                              //应用ID
            param.put("mch_id", partner);                           //商户ID号
            param.put("nonce_str", WXPayUtil.generateNonceStr());   //随机数
            param.put("body", "jkmall");                            	//订单描述
            param.put("out_trade_no",orderNo);                 //商户订单号
            param.put("total_fee", totalAmount);                      //交易金额
            param.put("spbill_create_ip", "127.0.0.1");           //终端IP
            param.put("notify_url", notifyurl);                    //回调地址
            param.put("trade_type", "NATIVE");                     //交易类型

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            ///3、执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:"+stringMap);

            //5、获取部分页面所需参数
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("code_url",stringMap.get("code_url"));
            dataMap.put("out_trade_no",orderNo);
            dataMap.put("total_fee",totalAmount);

            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map queryWechatpayStatus(String orderNo) {
        try {
            //1.封装参数
            Map param = new HashMap();
            param.put("appid",appid);                            //应用ID
            param.put("mch_id",partner);                         //商户号
            param.put("out_trade_no",orderNo);              //商户订单编号
            param.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param,partnerkey);

            //3、发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取返回值，并将返回值转成Map
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
