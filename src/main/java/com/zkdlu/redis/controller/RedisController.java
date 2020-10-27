package com.zkdlu.redis.controller;

import com.zkdlu.redis.service.RedisPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisPublisher redisPublisher;

    @GetMapping("/{channel}")
    public String publish(@PathVariable String channel, @RequestParam String message) {
        return "publish";
    }
}
