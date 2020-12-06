package com.zkdlu.redis.controller;

import com.zkdlu.redis.model.Data;
import com.zkdlu.redis.repo.ChannelRepository;
import com.zkdlu.redis.service.RedisPublisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final RedisPublisher redisPublisher;
    private final ChannelRepository channelRepository;

    public RedisController(RedisPublisher redisPublisher, ChannelRepository channelRepository) {
        this.redisPublisher = redisPublisher;
        this.channelRepository = channelRepository;
    }

    @GetMapping("/channel/{channel}")
    public String createChannel(@PathVariable String channel) {
        channelRepository.createTopic(channel);
        return channel;
    }

    @GetMapping("/{channel}")
    public String publish(@PathVariable String channel, @RequestBody Data data) {
        redisPublisher.publish(channelRepository.getTopic(channel), data);
        return "publish";
    }

    @GetMapping("/key/{key}")
    public String getValue(@PathVariable String key) {
        return channelRepository.getValue(key);
    }

    static int counter = 0;

    @Cacheable(key = "#count", value = "getCache")
    @GetMapping("/cache")
    public String getCache(String count) {
        counter++;
        return counter + "";
    }
}
