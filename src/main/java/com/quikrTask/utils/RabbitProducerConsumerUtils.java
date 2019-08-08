package com.quikrTask.utils;

import com.quikrTask.consumer.UserCommentConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducerConsumerUtils {

    @Value("${quikr_task_user_comment.config.exchange}")
    private String q_exchange;

    @Value("${quikr_task_user_comment.config.routingkey}")
    private String q_routing_key;

    @Value("${quikr_task_user_comment.config.queue}")
    private String q;

    @Value("${quikr_task_user_comment.config.consumertag}")
    private String q_consumer_tag;

    @Value("${quikr_task_user_comment.config.exchangeType}")
    private String q_exchange_type;

    private Channel userCommentChannel ;

    Logger logger = LoggerFactory.getLogger(RabbitProducerConsumerUtils.class);

    @Autowired
    private RabbitMQ rabbitMQ ;

    public void startConsumers() {
        logger.info("rabbit config ");
        try {
            initChannel();
            DefaultConsumer userCommentConsumer = new UserCommentConsumer(userCommentChannel);
            rabbitMQ.consume(userCommentConsumer, userCommentChannel, q, q_consumer_tag);

        } catch (Exception e) {
            logger.error("error while registering consumers {}", e);
        }
    }

    public void initChannel(){
        try {
            userCommentChannel = rabbitMQ.connect(q_exchange, q, q_exchange_type,  q_routing_key, 10);
        }catch(Exception e){
            logger.error("exception in initializing channels");
        }
    }

    public void publishToUserCommentQueue(Object object){
        try{
            String objectJson = JsonUtils.toJsonString(object) ;
            rabbitMQ.publish(objectJson.getBytes("UTF-8"), q_routing_key, userCommentChannel, q_exchange);
        }catch(Exception e){
            logger.error("error in publishing to rmq");
        }
    }

}
