package com.yyvax.iot.tymqtt.broker.persistence.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeServiceImpl.class);

    /**
     * <topic, List<(clientId, topicFilter)>>
     */
    private Map<String, List<Subscription>> subCache = new ConcurrentHashMap<>();

    /**
     * <clientId, List<subTopics>>
     */
    private Map<String, List<String>> subbedTopicsByClient = new ConcurrentHashMap<>();

    /**
     * <topic, <client, subscription>>
     */
    @Override
    public void put(String topicFilter, Subscription subscription) {
        String clientId = subscription.getClientId();
        // sub cache
        List<Subscription> clientsSubbed = subCache.containsKey(topicFilter) ? subCache.get(topicFilter) : new ArrayList<>();
        clientsSubbed.add(new Subscription(clientId, topicFilter));
        subCache.put(topicFilter, clientsSubbed);
        // sub topics by client
        List<String> topics = subbedTopicsByClient.getOrDefault(clientId, new ArrayList<>());
        topics.add(topicFilter);
        subbedTopicsByClient.put(clientId, topics);
        LOGGER.info("subscribe successfully! client - {}, topic - {}.", clientId, topicFilter);
    }

    @Override
    public void remove(String clientId, String topicFilter) {
        List<Subscription> subs = subCache.get(topicFilter);
        subs.removeIf(subscription -> subscription.getClientId().equals(clientId));
    }

    @Override
    public void removeByClient(String clientId) {
        List<String> topics = subbedTopicsByClient.get(clientId);
        topics.forEach(topic -> {
            remove(clientId, topic);
        });
        subbedTopicsByClient.remove(clientId);
    }

    @Override
    public List<Subscription> getSubscriptions(String topicFilter) {
        return subCache.get(topicFilter);
    }
}
