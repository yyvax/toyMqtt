package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.ClientSessionStore;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.Subscription;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Publish {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);

    private SubscribeService subscribeService;

    private ClientSessionStore clientSessionStore;

    public Publish(SubscribeService subscribeService, ClientSessionStore clientSessionStore) {
        this.subscribeService = subscribeService;
        this.clientSessionStore = clientSessionStore;
    }

    public void processPublish(Channel channel, MqttPublishMessage publishMessage) {
        // TODO: handle pub to a unconnected broker. After finishing session part
        switch (publishMessage.fixedHeader().qosLevel()) {
            case AT_MOST_ONCE:
                ByteBuf payload = publishMessage.payload();
                sendPublishMsg(publishMessage.variableHeader().topicName(), payload, publishMessage.fixedHeader().qosLevel());
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

    private void sendPublishMsg(String topic, ByteBuf payload, MqttQoS qoS) {
        List<Subscription> subscriptionClients = subscribeService.getSubscriptions(topic);
        subscriptionClients.forEach(subscriptionClient -> {
            if (qoS == MqttQoS.AT_MOST_ONCE) {
                MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttPublishVariableHeader(topic, 0),
                        payload
                );
                LOGGER.info("client id: {}", subscriptionClient);
                Channel channel = clientSessionStore.get(subscriptionClient.getClientId()).getChannel();
                channel.writeAndFlush(publishMessage.retain());
                LOGGER.info("publish successfully! topic '{}'", topic);
            }
        });

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
