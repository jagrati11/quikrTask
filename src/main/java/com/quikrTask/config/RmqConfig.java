package com.quikrTask.config;


import com.quikrTask.utils.RabbitMQ;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RmqConfig {

    @Value("${rabbitMQ.config.user}")
    private String userName;

    @Value("${rabbitMQ.config.password}")
    private String password;

    @Value("${rabbitMQ.config.host}")
    private String server;

    @Value("${rabbitMQ.config.port}")
    private Integer port;

    @Value("${rabbitMQ.comfig.virtualHost}")
    private String vhost;


    @Bean
    public RabbitMQ rabbitMQ(){
        RabbitMQ rabbitMQ = new RabbitMQ(server, port, userName, password, vhost);
        return rabbitMQ;
    }

}
