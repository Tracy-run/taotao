package com.demo.taotao.adapache.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *  将此文件放置F：
 *  cmd f:
 *  F:\> ab -n 100000 -c 1000 http://127.0.0.1:8080/xxxx/xxxx/xxx
 */
@RestController
@RequestMapping("/test")
public class ADTest implements SessionAwareMessageListener<Message> {

    Logger logger = LoggerFactory.getLogger(ADTest.class);

    @Resource(name = "taskExecutor")
    private ThreadPoolExecutor taskExecutor;

    @RequestMapping("/executor")
    @ResponseBody
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        taskExecutor.execute(() ->{
            try {
                logger.info("执行线程任务开始");
                Thread.currentThread().sleep(1000);
                if(logger.isDebugEnabled()){
                    logger.info("执行线程任务结束");
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        //设置线程的存活时间
        taskExecutor.setKeepAliveTime(60, TimeUnit.SECONDS);
    }
}
