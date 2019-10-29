package com.demo.taotao.rabbitMq.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){

            // queue持久化,在声明时指定durable => true
            channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);

            String message = String.join(" ",args);

            // 消息持久化
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));

            System.out.println("[X] Sent '" + message + "'");
        }

    }


}
