package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.ClientSessionStore;
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

    private Connect connect;

    private Publish publish;

    private Subscribe subscribe;

    public Connect connect() {
        connect = new Connect(clientSessionStore);
        LOGGER.info("Establish connection...");
        return connect;
    }

    public Publish publish() {
        publish = new Publish(subscribeService, clientSessionStore);
        LOGGER.info("Start publishing");
        return publish;
    }

    public Subscribe subscribe() {
        subscribe = new Subscribe(subscribeService);
        LOGGER.info("Start subscribe");
        return subscribe;
    }
}
