/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: Producer.java
 * Author:   Cheng Zhujiang
 * Date:     2017/10/18 21:53
 * Description: 
 */
package com.jemmy.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

/**
 * <pre>
 * Producer
 *
 * @author Cheng Zhujiang
 * @date 2017/10/18
 */
public class Producer {

    public static final String broker_url = "failover:(tcp://192.168.10.181:61616)";

    private static String queue_name = "test.queue";

    public static void main(String[] args) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, broker_url);
        // 通过工厂创建一个连接
        Connection connection = factory.createConnection();
        // 启动连接
        connection.start();
        // 创建一个session会话 事务 自动ack
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        // 创建一个消息队列
        Destination destination = session.createQueue(queue_name); // 如果已存在,使用已存在的
        // 创建生产者
        MessageProducer producer = session.createProducer(destination);
        // 消息持久化
//        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = session.createTextMessage("test delay message234:" + System.currentTimeMillis());
        long time = 60 * 1000; // 延时1min
        long period = 10 * 1000; // 每个10s
        int repeat = 6; // 6次
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
        message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
        // 发送消息
        producer.send(message);
        session.commit();
        producer.close();
        session.close();
        connection.close();
    }

}
