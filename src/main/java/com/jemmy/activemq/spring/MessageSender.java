/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: MessageSender.java
 * Author:   Cheng Zhujiang
 * Date:     2017/10/20 14:10
 * Description: 
 */
package com.jemmy.activemq.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

/**
 * <pre>
 * MessageSender
 *
 * @author Cheng Zhujiang
 * @date 2017/10/20
 */
public class MessageSender extends Thread {

    public static void main(String[] args) throws Exception {
        String[] configLocations = new String[] { "spring/applicationContext.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        JmsTemplate jmsTemplate = context.getBean("jmsTemplate", JmsTemplate.class);
        Destination destination = context.getBean("destination", Destination.class);
        for (int i = 1; i < 100; i++) {
            System.out.println("发送 i=" + i);
            //消息产生者
            MyMessageCreator myMessageCreator = new MyMessageCreator();
            myMessageCreator.n = i;
            jmsTemplate.send(destination, myMessageCreator);
            sleep(10000); //10秒后发送下一条消息
        }
    }
}
