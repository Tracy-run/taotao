package com.demo.taotao.activeMq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "demo.queue")
    public void readMessage(String message){
        System.out.println("接受到的消息是：" + message);
    }

}
