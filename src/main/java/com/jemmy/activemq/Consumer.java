/*
 * Copyright (C), 2014-2017, 杭州XX科技有限公司
 * FileName: Consumer.java
 * Author:   Cheng Zhujiang
 * Date:     2017/10/19 20:16
 * Description: 
 */
package com.jemmy.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * Consumer
 *
 * @author Cheng Zhujiang
 * @date 2017/10/19
 */
public class Consumer {

    public static final String broker_url = "failover:(tcp://192.168.10.181:61616)";

    public static String queue_name = "test.queue";

    public static void main(String[] args) throws JMSException, InterruptedException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, broker_url);
        // 通过工厂创建一个连接
        Connection connection = factory.createConnection();
        // 启动连接
        connection.start();
        // 创建一个session会话 事务 自动ack
        Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
        // 创建一个消息队列
        Destination destination = session.createQueue(queue_name);
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("receive message : " + ((TextMessage)message).getText());
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        new CountDownLatch(1).await();
    }
}
