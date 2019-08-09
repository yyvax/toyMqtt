package com.yyvax.iot.tymqtt.broker.persistence.session;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class ClientSession {

    private String clientId;

    private Channel channel;

    private boolean cleanSession;

    public ClientSession(String clientId, Channel channel, boolean cleanSession) {
        this.channel = channel;
        this.clientId = clientId;
        this.cleanSession = cleanSession;
    }


}
