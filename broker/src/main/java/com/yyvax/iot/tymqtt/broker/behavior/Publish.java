package com.yyvax.iot.tymqtt.broker.behavior;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publish {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);

    public Publish() {

    }

    public void processPublish(Channel channel, MqttPublishMessage publishMessage) {
        // TODO: handle pub to a unconnected broker. After finishing session part
        switch (publishMessage.fixedHeader().qosLevel()) {
            case AT_MOST_ONCE:
                ByteBuf payload = publishMessage.payload();

                break;
            case AT_LEAST_ONCE:
                break;
            case EXACTLY_ONCE:
                break;
            case FAILURE:
                break;
            default:
                break;
        }

    }

    private void sendPublishMsg() {

    }

    private void sendPubAckMsg(Channel channel, int msgId) {
        MqttPubAckMessage pubAckMessage = (MqttPubAckMessage)
                MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.PUBACK,
                                false, MqttQoS.AT_LEAST_ONCE, false, 2),
                       MqttMessageIdVariableHeader.from(msgId), null);
        channel.writeAndFlush(pubAckMessage);
    }

    private void snedPubRecMsg(Channel channel, int msgId) {
        MqttMessage pubRecMessage = MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.PUBREC,
                false, MqttQoS.AT_LEAST_ONCE, false, 2),
                MqttMessageIdVariableHeader.from(msgId), null);
        channel.writeAndFlush(pubRecMessage);
    }

}
