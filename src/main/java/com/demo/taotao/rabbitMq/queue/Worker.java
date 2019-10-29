package com.demo.taotao.rabbitMq.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {

    private static final String  TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        System.out.println("[*] waiting for messages. To exit press ctrl+c");

        //通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
        // accept only one unack-ed message at a time (see below)
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) ->{
            String message = new String(delivery.getBody(),"UTF-8");

            System.out.println("[X] received '" + message + "'" );
            try{
                dowork(message);
            }finally {
                System.out.println("[X] done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,consumerTag -> {});
    }

    private static void dowork(String task){
        for(char ch:task.toCharArray()){
            if(ch == '.'){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}
