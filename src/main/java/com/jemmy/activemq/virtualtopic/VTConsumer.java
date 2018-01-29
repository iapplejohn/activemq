/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: VTConsumer.java
 * Author:   Cheng Zhujiang
 * Date:     2017/11/17 12:47
 * Description: 
 */
package com.jemmy.activemq.virtualtopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * VTConsumer
 *
 * @author Cheng Zhujiang
 * @date 2017/11/17
 */
public class VTConsumer {

    public static void main(String[] args) throws JMSException, InterruptedException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.10.116:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建主题
        Queue topicA = session.createQueue("Consumer.A.VirtualTopic.TEST");
        Queue topicB = session.createQueue("Consumer.B.VirtualTopic.TEST");
        // 消费者A组创建订阅
        MessageConsumer consumerA1 = session.createConsumer(topicA);
        consumerA1.setMessageListener(new MessageListener() {
            // 订阅接收方法
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println("Received message A1: " + tm.getText() + ":" + tm.getStringProperty("property"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        MessageConsumer consumerA2 = session.createConsumer(topicA);
        consumerA2.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println("Received message A2: " + tm.getText() + ":" + tm.getStringProperty("property"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //消费者B组创建订阅
        MessageConsumer consumerB1 = session.createConsumer(topicB);
        consumerB1.setMessageListener(new MessageListener() {
            // 订阅接收方法
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println("Received message B1: " + tm.getText() + ":" + tm.getStringProperty("property"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        MessageConsumer consumerB2 = session.createConsumer(topicB);
        consumerB2.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println("Received message B2: " + tm.getText() + ":" + tm.getStringProperty("property"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        TimeUnit.SECONDS.sleep(60);
        session.close();
        connection.close();
    }
}
