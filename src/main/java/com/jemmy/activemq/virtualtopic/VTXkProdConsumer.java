package com.jemmy.activemq.virtualtopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Connection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/7.
 */
public class VTXkProdConsumer {

    public static void main(String[] args) throws JMSException, InterruptedException {
        Connection connection = null;
        Session session = null;
        try {
            doCreateConsumer(connection, session);
        } finally {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        TimeUnit.SECONDS.sleep(30);
    }

    private static void doCreateConsumer(Connection connection, Session session) throws JMSException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://10.10.86.26:61616");
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

        Session finalSession = session;
        Connection finalConnection = connection;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (finalSession != null) {
                    try {
                        finalSession.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    finalConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }));

        // 创建主题
        Queue topicA = session.createQueue("Consumer.stmt.VirtualTopic.order_test_event");
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
    }
}
