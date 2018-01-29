/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: MessageEventFactory.java
 * Author:   Cheng Zhujiang
 * Date:     2017/11/17 16:10
 * Description: 
 */
package com.jemmy.activemq.tool;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * <pre>
 * MessageEventFactory
 *
 * @author Cheng Zhujiang
 * @date 2017/11/17
 */
@Component
public class MessageEventFactory implements InitializingBean {

    /** Logger */
    private static Logger LOGGER = LoggerFactory.getLogger(MessageEventFactory.class);

    private static String active_topic = "VirtualTopic.";

    private static String TRACE_ID = "traceId";
    private static String TRACE_URL = "traceUrl";

    @Resource
    ConnectionFactory jmsConnFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        registerMessageListener();
        registerReplyMessageListener();
    }

    private void registerMessageListener() {

    }

    private void registerReplyMessageListener() {

    }

    public void publishEvent(QtMessageEvent event) {
        boolean transacted = event.transactional() && TransactionSynchronizationManager.isActualTransactionActive();
        if (transacted) {
            _publishEventTransactional(event);
        } else {
            _publishEvent(event);
        }
    }

    private void _publishEventTransactional(final QtMessageEvent event) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    _publishEvent(event);
                } catch (Exception e) {
                    LOGGER.error("消息内容:{},错误:{}", event, e); // TODO event转成json
                    // TODO 分布式消息存储
                }
            }

            @Override
            public void afterCompletion(int status) {
                if (status != TransactionSynchronization.STATUS_COMMITTED) {
                    LOGGER.warn("撤销消息发送,事务状态:{},消息内容:{}", status, event); // TODO event转成json
                    // TODO 分布式消息存储
                }
            }
        });
    }

    private void _publishEvent(QtMessageEvent event) {
        String name = event.name();
        Connection conn = null;
        Session session = null;
        String msgContent = "";

        try {
            msgContent = event.toString(); // event转json
            conn = jmsConnFactory.createConnection();
            session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(new ActiveMQTopic(active_topic + getZoneProjectName(name)));
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage message = session.createTextMessage(msgContent);
            message.setStringProperty(TRACE_ID, ""); // TODO
            message.setStringProperty(TRACE_URL, ""); // TODO
            if (StringUtils.isNotBlank(event.getConsumerGroup())) {
                message.setStringProperty("groupId", event.getConsumerGroup());
            }
            producer.send(message);
            // TODO 分布式消息存储
        } catch (JMSException e) {
            // TODO 分布式消息存储
            // TODO 抛出异常
        } finally {
            closeSession(session);
            closeConnection(conn);
        }
    }

    private String getZoneProjectName(String name) {
        return name;
    }

    private void closeSession(Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
