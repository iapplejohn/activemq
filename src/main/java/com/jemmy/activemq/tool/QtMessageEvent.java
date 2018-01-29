/*
 * Copyright (C), 2014-2017, 杭州小卡科技有限公司
 * FileName: QtMessageEvent.java
 * Author:   Cheng Zhujiang
 * Date:     2017/11/17 16:14
 * Description: 
 */
package com.jemmy.activemq.tool;

/**
 * <pre>
 * QtMessageEvent
 *
 * @author Cheng Zhujiang
 * @date 2017/11/17
 */
public interface QtMessageEvent {

    /**
     * 事件名称
     * @return
     */
    String name();

    /**
     * 是否开启事务
     * true: 当前推送消息处于jdbc事务环境当中，那么等jdbc事务提交完成后消息才会被推送出去
     * @return
     */
    default boolean transactional() {
        return false;
    }

    /**
     * 消费组，请使用类似订单ID的流水数据,保证消费方的执行顺序
     * @return
     */
    default String getConsumerGroup() {
        return null;
    }
}
