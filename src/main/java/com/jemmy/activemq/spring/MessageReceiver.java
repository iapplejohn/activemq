/*
 * Copyright (C), 2014-2017, 杭州XX科技有限公司
 * FileName: MessageReceiver.java
 * Author:   Cheng Zhujiang
 * Date:     2017/10/20 14:58
 * Description: 
 */
package com.jemmy.activemq.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * <pre>
 * MessageReceiver
 *
 * @author Cheng Zhujiang
 * @date 2017/10/20
 */
public class MessageReceiver {

    public static void main(String[] args) throws JMSException {
        String[] configLocations = new String[] { "spring/applicationContext.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        JmsTemplate jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
        Destination destination = context.getBean("destination", Destination.class);
        TextMessage msg = null;
        //是否继续接收消息
        boolean isContinue = true;
        while (isContinue) {
            msg = (TextMessage) jmsTemplate.receive(destination);
            System.out.println("收到消息 :" + msg.getText());
            if ("end".equals(msg.getText())) {
                isContinue = false;
                System.out.println("收到退出消息，程序要退出！");
            }
        }
        System.out.println("程序退出了！");
    }
}
