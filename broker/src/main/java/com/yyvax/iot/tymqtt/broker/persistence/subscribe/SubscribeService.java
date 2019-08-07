package com.yyvax.iot.tymqtt.broker.persistence.subscribe;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubscribeService {
    /**
     * save subscription.
     */
    void put(String topicFilter, Subscription subscription);

    void remove(String clientId, String topicFilter);

    void removeByClient(String clientId);

    /**
     * @param topicFilter topic name
     * @return list of subscriptions
     */
    List<Subscription> getSubscriptions(String topicFilter);
}
