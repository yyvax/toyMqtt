package com.yyvax.iot.tymqtt.broker.mqttbehavior;

import org.springframework.stereotype.Component;

@Component
public class Processor {

    Connect connect;

    public Connect connect() {
        return new Connect();
    }
}
