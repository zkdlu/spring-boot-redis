# Redis
- REmote Dictionary Server, 메모리 기반의 "키-값" 구조 데이터 관리 시스템. 비 관계형 데이터베이스
- 모든 데이터를 메모리에 저장하고 조회하여 Read/Write가 빠름.
- String, Set, Sorted Set, Hash, List 5가지 데이터 형식 지원

## Docker로 CLI 접속
```
docker exec -it redis-container redis-cli
```

### 기본 명령어
- 전체 Key
```
keys *
```

- 데이터 삽입
```
set {key} {value}
```

- 데이터 조회
```
get {key}
```

- Sub (구독)
```
subscribe [channel] // 여러 채널 구독 가능
```

- PUB (게시)
```
publish {channel} {message}
```

- channel 목록
```
pubsub channels
pubsub channels *
```

### Spring에서 Redis를 이용하는 방법
1. 의존성 추가
```build.gradle
dependencies {
   implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```

2. Redis Config
```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }
}
```

3. StringRedisTemplate, RedisTemplate 사용하기
   > opsForValue를 통해서 레디스에 Key-Value 기반인 데이터를 캐싱하거나 그 값을 얻어올 수 있음
4. CrudRepository 사용하기
   > @RedisHash로 모델 클래스가 Redis에 적재될 때 인자를 키로 해당 인스턴스 값을 적재 함

## Redis는 캐시로도 사용된다.

### Redis Cache 사용하기
1. application.properties에 옵션 추가
```properties
spring.cache.type=redis  # 요거
spring.redis.host=localhost
spring.redis.port=6379
```
2. Main 클래스에 @EnableCaching 어노테이션 추가
```java
@EnableCaching
@SpringBootApplication
.. 중략
```

3. Cache Config 설정
```java
@Configuration
public class CacheConfig {
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisConnectionFactory connectionFactory;

    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
    }
}
```

4. 사용하기
```java
    static int counter = 0;

    @Cacheable(key = "#count", value = "getCache")
    @GetMapping("/cache")
    public String getCache(String count) {
        counter++;
        return counter + "";
    }
```
> http://localhost:8080/cache?count=1로 요청을 보내도 처음 이후에는 counter값이 증가하지않음
> - 캐시 등록(없으면) @Cacheable
> - 캐시 등록(무조건) @CachePut
> - 캐시 삭제 @Cacheable

## Redis Cluster??
