/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: MyMessageCreator.java
 * Author:   Cheng Zhujiang
 * Date:     2017/10/20 13:44
 * Description: 
 */
package com.jemmy.activemq.spring;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * <pre>
 * MyMessageCreator
 *
 * @author Cheng Zhujiang
 * @date 2017/10/20
 */
public class MyMessageCreator implements MessageCreator {

    public int n = 0;

    private static String str1 = "这个是第 ";

    private static String str2 = " 个测试消息！";

    private String str = "";

    @Override
    public Message createMessage(Session session) throws JMSException {
        System.out.println("MyMessageCreator  n=" + n);
        if (n == 9) {
            //在这个例子中表示第9次调用时，发送结束消息
            return session.createTextMessage("end");
        }
        str = str1 + n + str2;
        return session.createTextMessage(str);
    }
}
