package com.yyvax.iot.tymqtt.broker.mqttbehavior;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);

    public Connect() {

    }

    public void processConnect(Channel channel, MqttConnectMessage connectMessage) {

    }

}
