package com.jkmall.order.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    /**
     * 短信发送队列
     * @return
     */
    @Bean
    public Queue messageQueue() {
        return new Queue("orderListenerQueue", true);
    }

    /**
     * 短信发送队列
     * @return
     */
    @Bean
    public Queue delayMessageQueue() {
        return QueueBuilder.durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange", "orderListenerExchange")        // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", "orderListenerQueue")   // 绑定指定的routing-key
                .build();
    }

    /***
     * 创建交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("orderListenerExchange");
    }


    /***
     * 交换机与队列绑定
     * @param messageQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding basicBinding(Queue messageQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(messageQueue)
                .to(directExchange)
                .with("orderListenerQueue");
    }
}
