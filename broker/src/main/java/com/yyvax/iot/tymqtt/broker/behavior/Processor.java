package com.yyvax.iot.tymqtt.broker.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private Connect connect;

    private Publish publish;

    public Connect connect() {
        connect = new Connect();
        LOGGER.info("Establish connection...");
        return connect;
    }

    public Publish publish() {
        publish = new Publish();
        LOGGER.info("Start publishing");
        return publish;
    }
}
