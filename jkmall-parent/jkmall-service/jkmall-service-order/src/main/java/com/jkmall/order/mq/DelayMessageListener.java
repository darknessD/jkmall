package com.jkmall.order.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "orderListenerQueue")
public class DelayMessageListener {

    @RabbitHandler
    public void getDelayMessage(String message){
        System.out.println("Delay Message: " + message);

    }
}
