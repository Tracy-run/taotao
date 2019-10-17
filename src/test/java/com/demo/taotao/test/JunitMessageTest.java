package com.demo.taotao.test;

import com.demo.taotao.TaotaoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Destination;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TaotaoApplication.class)
public class JunitMessageTest {

    @Autowired
    private Destination destination;

    @Autowired
    private JmsTemplate jmsTemplate;


    @Test
    public void test(){
        System.out.println("我来了。。。。。。。。");
        this.jmsTemplate.convertAndSend(destination,"你好啊  外星人！");
    }


}
