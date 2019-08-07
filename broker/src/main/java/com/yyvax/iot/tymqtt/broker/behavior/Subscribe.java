package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.Subscription;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Subscribe {
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscribe.class);

    SubscribeService subscribeService;

    public Subscribe(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    public void processSubscribe(Channel channel, MqttSubscribeMessage mqttSubscribeMessage) {
        List<MqttTopicSubscription> subscribeMessages = mqttSubscribeMessage.payload().topicSubscriptions();
        for (MqttTopicSubscription subscribeMessage : subscribeMessages) {
            String topicFilter = subscribeMessage.topicName();
            String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
            Subscription subscription = new Subscription(clientId, topicFilter);
            subscribeService.put(topicFilter, subscription);
            // subAck
            MqttSubAckMessage mqttSubAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    MqttMessageIdVariableHeader.from(mqttSubscribeMessage.variableHeader().messageId()),
                    new MqttSubAckPayload(0)
            );
            channel.writeAndFlush(mqttSubAckMessage);
        }
    }
}
