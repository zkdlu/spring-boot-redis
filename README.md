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

### Spring에서 Redis를 이용하는 방법
1. 의존성 추가
```build.gradle
dependencies {
   implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```
1. StringRedisTemplate, RedisTemplate 사용하기
   > opsForValue를 통해서 레디스에 Key-Value 기반인 데이터를 캐싱하거나 그 값을 얻어올 수 있음
2. CrudRepository 사용하기
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
