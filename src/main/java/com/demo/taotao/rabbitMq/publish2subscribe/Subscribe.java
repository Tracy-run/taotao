package com.demo.taotao.rabbitMq.publish2subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Subscribe {

    private static final String  EXCHANEG_NAME = "logs";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANEG_NAME,"fanout");

        String queueName = channel.queueDeclare().getQueue();
        //将queue 与exchange_name 绑定
        channel.queueBind(queueName,EXCHANEG_NAME,"");

        System.out.println("[X] waiting for messages. To exit press ctrl+c");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("[x] received '" + message + "'");
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }


}
