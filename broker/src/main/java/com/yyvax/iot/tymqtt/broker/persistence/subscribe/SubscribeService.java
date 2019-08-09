package com.yyvax.iot.tymqtt.broker.persistence.subscribe;

import java.util.List;

public interface SubscribeService {
    /**
     * save subscription.
     */
    void put(String topicFilter, Subscription subscription);

    /**
     * Remove specific topic by client
     * @param clientId client id
     * @param topicFilter topic
     */
    void remove(String clientId, String topicFilter);

    /**
     * When disconnect with clean session, unsub all the topics
     * @param clientId client id
     */
    void removeByClient(String clientId);

    /**
     * @param topicFilter topic name
     * @return list of subscriptions
     */
    List<Subscription> getSubscriptions(String topicFilter);
}
