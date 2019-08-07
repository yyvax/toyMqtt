package com.yyvax.iot.tymqtt.broker.persistence.subscribe;

import lombok.Data;

@Data
public class Subscription {

    private String clientId;

    private String topicFilter;

    public Subscription(String clientId, String topicFilter) {
        this.clientId = clientId;
        this.topicFilter = topicFilter;
    }

}
