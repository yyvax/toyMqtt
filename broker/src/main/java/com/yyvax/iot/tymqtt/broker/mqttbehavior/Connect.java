package com.yyvax.iot.tymqtt.broker.mqttbehavior;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);

    public Connect() {

    }

    public void processConnect(Channel channel, MqttConnectMessage connectMessage) {
        LOGGER.info("Start connecting to broker");
        boolean sessionPresent = !connectMessage.variableHeader().isCleanSession();
        // TODO: first handle exceptions
        if (connectMessage.decoderResult().isFailure()) {
        }

        // return connAck msg
        MqttConnAckMessage connAckMessage = createConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent);
        channel.writeAndFlush(connAckMessage);
    }

    private MqttConnAckMessage createConnAckMsg(MqttConnectReturnCode connectReturnCode, boolean sessionPresent) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false,
                MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(connectReturnCode, sessionPresent);
        return new MqttConnAckMessage(mqttFixedHeader, variableHeader
        );
    }

}
