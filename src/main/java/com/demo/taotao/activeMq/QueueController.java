package com.demo.taotao.activeMq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/**
 * 发送消息
 */
@RestController
@RequestMapping(value = "/queue")
public class QueueController {

    @Autowired
    private JmsTemplate jmsTemplate;


    @Autowired
    private Destination destination;

    @RequestMapping("send/{message}")
    public String send(@PathVariable String message){
        this.jmsTemplate.convertAndSend(destination,message);
        return "消息发送成功，内容是：" + message;
    }


}
