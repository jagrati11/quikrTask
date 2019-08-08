package com.quikrTask;

import com.quikrTask.config.RmqConfig;
import com.quikrTask.utils.RabbitMQ;
import com.quikrTask.utils.RabbitProducerConsumerUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        RabbitProducerConsumerUtils rabbitProducerConsumerUtils = context.getBean(RabbitProducerConsumerUtils.class);
        rabbitProducerConsumerUtils.startConsumers();
    }
}
