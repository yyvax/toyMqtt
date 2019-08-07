package com.yyvax.iot.tymqtt.broker.persistence;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class ClientSession {

    private String clientId;

    private Channel channel;

    public ClientSession(String clientId, Channel channel) {
        this.channel = channel;
        this.clientId = clientId;
    }

}
