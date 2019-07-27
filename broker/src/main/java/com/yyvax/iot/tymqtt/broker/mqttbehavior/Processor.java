package com.yyvax.iot.tymqtt.broker.mqttbehavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private Connect connect;

    public Connect connect() {
        connect = new Connect();
        LOGGER.info("Establish connection...");
        return connect;
    }
}
