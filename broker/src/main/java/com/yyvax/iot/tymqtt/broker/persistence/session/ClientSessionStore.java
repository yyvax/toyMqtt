package com.yyvax.iot.tymqtt.broker.persistence.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientSessionStore {

    Map<String, ClientSession> sessionMap = new ConcurrentHashMap<>();

    public void put(String clientId, ClientSession clientSession) {
         sessionMap.put(clientId, clientSession);
    }

    public ClientSession get(String clientId) {
        return sessionMap.get(clientId);
    }

    public void remove(String clientId) {
        sessionMap.remove(clientId);
    }
}
