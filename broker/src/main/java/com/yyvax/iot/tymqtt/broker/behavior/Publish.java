package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.session.ClientSessionStore;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.Subscription;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        ByteBuf payload = publishMessage.payload();
        switch (publishMessage.fixedHeader().qosLevel()) {
            case AT_MOST_ONCE:
                sendPublishMsg(publishMessage.variableHeader().topicName(), payload, MqttQoS.AT_MOST_ONCE);
                break;
            case AT_LEAST_ONCE:
                sendPublishMsg(publishMessage.variableHeader().topicName(), payload, MqttQoS.AT_LEAST_ONCE);
                sendPubAckMsg(channel, publishMessage.variableHeader().packetId());
                break;
            case EXACTLY_ONCE:
                sendPublishMsg(publishMessage.variableHeader().topicName(), payload, MqttQoS.EXACTLY_ONCE);
                sendPubRecMsg(channel, publishMessage.variableHeader().packetId());
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
            MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, qoS, false, 0),
                    new MqttPublishVariableHeader(topic, 0),
                    payload
            );
            LOGGER.info("client id: {}", subscriptionClient);
            Channel channel = clientSessionStore.get(subscriptionClient.getClientId()).getChannel();
            channel.writeAndFlush(publishMessage.retain());
            LOGGER.info("publish successfully! topic '{}', QoS:{}", topic, qoS);
        });

    }

    private void sendPubAckMsg(Channel channel, int msgId) {
        MqttPubAckMessage pubAckMessage = (MqttPubAckMessage)
                MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.PUBACK,
                                false, MqttQoS.AT_LEAST_ONCE, false, 2),
                        MqttMessageIdVariableHeader.from(msgId), null);
        channel.writeAndFlush(pubAckMessage);
    }

    private void sendPubRecMsg(Channel channel, int msgId) {
        MqttMessage pubRecMessage = MqttMessageFactory.newMessage(new MqttFixedHeader(MqttMessageType.PUBREC,
                        false, MqttQoS.AT_LEAST_ONCE, false, 2),
                MqttMessageIdVariableHeader.from(msgId), null);
        channel.writeAndFlush(pubRecMessage);
    }

}
