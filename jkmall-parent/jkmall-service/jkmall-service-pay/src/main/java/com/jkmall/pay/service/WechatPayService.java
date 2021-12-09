package com.jkmall.pay.service;

import java.util.Map;

public interface WechatPayService {

    Map createWechatPay(String orderNo, String totalAmount);

    Map queryWechatpayStatus(String orderNo);
}
