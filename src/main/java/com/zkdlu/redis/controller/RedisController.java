package com.zkdlu.redis.controller;

import com.zkdlu.redis.model.Data;
import com.zkdlu.redis.repo.RedisRepository;
import com.zkdlu.redis.service.RedisPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final RedisPublisher redisPublisher;
    private final RedisRepository redisRepository;

    public RedisController(RedisPublisher redisPublisher, RedisRepository redisRepository) {
        this.redisPublisher = redisPublisher;
        this.redisRepository = redisRepository;
    }

    @GetMapping("/channel/{channel}")
    public String createChannel(@PathVariable String channel) {
        redisRepository.createTopic(channel);

        return channel;
    }

    @GetMapping("/{channel}")
    public String publish(@PathVariable String channel, @RequestBody Data data) {
        redisPublisher.publish(redisRepository.getTopic(channel), data);

        return "publish";
    }
}
