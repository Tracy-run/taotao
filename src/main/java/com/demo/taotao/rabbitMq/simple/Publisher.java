package com.demo.taotao.rabbitMq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {

    //https://blog.csdn.net/whoamiyang/article/details/54954780

    private final static String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){

            //声明消息队列，且为可持久化的
            // channel.ExchangeDeclare(ExchangeName, "direct", durable: true, autoDelete: false, arguments: null);

            //queue持久化,在声明时指定durable => true
            //channel.QueueDeclare(QueueName, durable: true, exclusive: false, autoDelete: false, arguments: null); //声明消息队列，且为可持久化的
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
            // channel.basic_qos(prefetch_count=1);

            String message = "hello world!";

            //消息持久化
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("utf-8"));

            System.out.println("[X] sent '" + message + "'");
        }
    }


}
