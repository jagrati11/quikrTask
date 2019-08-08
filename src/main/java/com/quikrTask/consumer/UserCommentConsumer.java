package com.quikrTask.consumer;

import com.quikrTask.repository.ESRepository;
import com.quikrTask.utils.ApplicationContextProvider;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import java.io.IOException;
import java.nio.charset.Charset;


public class UserCommentConsumer extends DefaultConsumer {

    private Channel channel;
    Logger logger = LoggerFactory.getLogger(UserCommentConsumer.class);

    public UserCommentConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }


    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();
        String requestBody = new String(body, Charset.forName("utf-8"));
        try {
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext() ;
            ESRepository esRepository = applicationContext.getBean(ESRepository.class) ;
            esRepository.index("user_comment", requestBody);

        } catch (Exception e) {
            logger.error("[DmsLeadConsumer] exception occurred ::{}", e);
        } finally {
             channel.basicAck(deliveryTag, false);
        }
    }

}
