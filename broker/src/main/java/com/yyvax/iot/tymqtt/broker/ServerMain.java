package com.yyvax.iot.tymqtt.broker;

import com.yyvax.iot.tymqtt.broker.config.BrokerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.yyvax.iot.tymqtt.broker")
public class ServerMain {

    @Autowired
    private BrokerConfig brokerConfig;

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }

}
