package com.yyvax.iot.tymqtt.broker.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
public class BrokerConfig {
    private int id;

    @Value("${broker.host}")
    private String host;

    @Value("${broker.port}")
    private int port;


}
