package com.quikrTask.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

    private ConnectionFactory connectionFactory;
    Logger logger = LoggerFactory.getLogger(RabbitProducerConsumerUtils.class);

    public RabbitMQ(String server, Integer port, String userName, String password, String vhost){
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(server);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        connectionFactory.setAutomaticRecoveryEnabled(true);
    }

    public Channel connect(String exchange, String queue, String exchangeType, String routingKey, int prefetchCount) throws IOException, TimeoutException {
        try{
            Connection connection = null;
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, exchangeType,true);
            channel.queueDeclare(queue, false, false,false,null);
            if(routingKey == null){
                routingKey = "*";
            }
            channel.queueBind(queue, exchange, routingKey);
            channel.basicQos(prefetchCount);
            return channel;
        }catch (IOException ex) {
            logger.error("Error in RabbitMQ connect for exchange: " + exchange + " queue: " + queue + " exchangeType: "
                    + exchangeType + " routingKey: " + routingKey + " \nError: " + ex);
            throw ex;
        } catch (TimeoutException ex) {
            logger.error("TimeoutException in RabbitMQ connect for exchange: " + exchange + " queue: " + queue
                    + " exchangeType: " + exchangeType + " routingKey: " + routingKey + " \nError: " + ex);
            throw ex;
        }
    }

    public void publish(byte[] message, String routingKey, Channel channel,String exchange) throws IOException{
        try{
            channel.basicPublish(exchange, routingKey, null, message);
        }catch (Exception e){
            logger.error("Error in RabbitMQ publish : "+e.getMessage(), e);
            throw e;
        }
    }

    public void consume(DefaultConsumer defaultConsumer, Channel channel, String queue, String tagName) throws IOException{
        channel.basicConsume(queue, false, tagName, defaultConsumer);
    }
}
