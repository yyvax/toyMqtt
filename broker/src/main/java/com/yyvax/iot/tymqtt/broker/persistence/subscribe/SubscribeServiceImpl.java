package com.yyvax.iot.tymqtt.broker.persistence.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeServiceImpl.class);

    Map<String, List<Subscription>> subCache = new ConcurrentHashMap<>();

    /**
     * <topic, <client, subscription>>
     */
    @Override
    public void put(String topicFilter, Subscription subscription) {
        String clientId = subscription.getClientId();
        List<Subscription> clientsSubbed = subCache.containsKey(topicFilter) ? subCache.get(topicFilter) : new ArrayList<>();
        clientsSubbed.add(new Subscription(clientId, topicFilter));
        subCache.put(topicFilter, clientsSubbed);
        LOGGER.info("subscribe successfully! client - {}, topic - {}.", clientId, topicFilter);
    }

    @Override
    public void remove(String clientId, String topicFilter) {

    }

    @Override
    public void removeByClient(String clientId) {

    }

    @Override
    public List<Subscription> getSubscriptions(String topicFilter) {
        return subCache.get(topicFilter);
    }
}
