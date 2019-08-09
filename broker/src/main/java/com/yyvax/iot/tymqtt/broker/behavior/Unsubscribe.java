package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Unsubscribe {

    private static final Logger LOGGER = LoggerFactory.getLogger(Unsubscribe.class);

    private SubscribeService subscribeService;

    public Unsubscribe(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    public void processUnsubscribe(Channel channel, MqttUnsubscribeMessage unsubscribeMessage) {
        List<String> topics = unsubscribeMessage.payload().topics();
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        topics.forEach(topic -> {
            subscribeService.remove(clientId, topic);
            LOGGER.info("Unsubscribe client: {}, topic: {}.", clientId, topic);
        });
        // write UnsubAck msg
        MqttUnsubAckMessage unsubAckMessage = new MqttUnsubAckMessage(new MqttFixedHeader(MqttMessageType.UNSUBACK,
                false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(unsubscribeMessage.variableHeader().messageId()));
        channel.writeAndFlush(unsubAckMessage);
    }
}
