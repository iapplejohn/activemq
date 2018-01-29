/*
 * Copyright (C), 2014-2017, 杭州XX科技有限公司
 * FileName: VTProducer.java
 * Author:   Cheng Zhujiang
 * Date:     2017/11/16 22:21
 * Description: 
 */
package com.jemmy.activemq.virtualtopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * <pre>
 * VTProducer
 *
 * @author Cheng Zhujiang
 * @date 2017/11/16
 */
public class VTProducer {

    public static void main(String[] args) throws JMSException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.10.116:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建主题
        Topic topic = session.createTopic("VirtualTopic.TEST");
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = session.createTextMessage();
        message.setText("topic 消息4。");
        message.setStringProperty("property", "消息property4");
        // 发布主题消息
        producer.send(message);
        System.out.println("Message: " + message.getText() + " is sent");
        session.close();
        connection.close();
    }
}
