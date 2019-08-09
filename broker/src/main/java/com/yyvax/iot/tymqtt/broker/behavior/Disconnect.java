package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.session.ClientSessionStore;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disconnect {

    private static final Logger LOGGER = LoggerFactory.getLogger(Disconnect.class);

    private ClientSessionStore clientSessionStore;

    private SubscribeService subscribeService;

    public Disconnect(ClientSessionStore clientSession, SubscribeService subscribeService) {
        this.clientSessionStore = clientSession;
        this.subscribeService = subscribeService;
    }

    public void processDisconnect(Channel channel) {
         String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
         // remove all the subs if clean session
         if (clientSessionStore.get(clientId).isCleanSession()) {
             subscribeService.removeByClient(clientId);
             LOGGER.info("cleanSession: true, Removed all subs.");
         }
         clientSessionStore.remove(clientId);
         channel.close();
         LOGGER.info("Client:{} disconnected.", clientId);
    }
}
