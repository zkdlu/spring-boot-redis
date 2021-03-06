package com.zkdlu.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkdlu.redis.model.Data;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;

    private ValueOperations<String, Object> opsValue;

    public RedisSubscriber(ObjectMapper objectMapper, RedisTemplate redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        opsValue = redisTemplate.opsForValue();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            Data data = objectMapper.readValue(publishMessage, Data.class);
            System.out.println(data.getKey() + ": " + data.getValue());

            opsValue.set(data.getKey(), data.getValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
