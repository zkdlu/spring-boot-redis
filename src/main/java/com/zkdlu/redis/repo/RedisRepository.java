package com.zkdlu.redis.repo;

import com.zkdlu.redis.service.RedisSubscriber;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RedisRepository {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    private Map<String, ChannelTopic> topics;
    private ValueOperations<String, Object> opsValue;

    public RedisRepository(RedisSubscriber redisSubscriber, RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer redisMessageListener) {
        this.redisSubscriber = redisSubscriber;
        this.redisTemplate = redisTemplate;
        this.redisMessageListener = redisMessageListener;
    }

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
        opsValue = redisTemplate.opsForValue();
    }

    public ChannelTopic getTopic(String channel) {
        ChannelTopic topic = topics.get(channel);
        if (topic == null) {
            topic = createTopic(channel);
        }
        return topic;
    }

    public ChannelTopic createTopic(String channel) {
        ChannelTopic topic = new ChannelTopic(channel);

        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(channel, topic);

        return topic;
    }
}
