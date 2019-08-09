package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.session.ClientSessionStore;
import com.yyvax.iot.tymqtt.broker.persistence.subscribe.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private ClientSessionStore clientSessionStore;

    public Connect connect() {
        Connect connect = new Connect(clientSessionStore);
        LOGGER.info("Establish connection...");
        return connect;
    }

    public Disconnect disconnect() {
        Disconnect disconnect = new Disconnect(clientSessionStore, subscribeService);
        LOGGER.info("Start disconnecting...");
        return disconnect;
    }

    public Publish publish() {
        Publish publish = new Publish(subscribeService, clientSessionStore);
        LOGGER.info("Start publishing...");
        return publish;
    }

    public Subscribe subscribe() {
        Subscribe subscribe = new Subscribe(subscribeService);
        LOGGER.info("Start subscribing...");
        return subscribe;
    }

    public Unsubscribe unsubscribe() {
        Unsubscribe unsubscribe = new Unsubscribe(subscribeService);
        LOGGER.info("Start unsubscribing...");
        return unsubscribe;
    }

    public PingReq pingReq() {
        PingReq pingReq = new PingReq();
        LOGGER.info("Received ping...");
        return pingReq;
    }
}
