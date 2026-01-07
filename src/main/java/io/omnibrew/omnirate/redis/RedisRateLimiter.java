package io.omnibrew.omnirate.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class RedisRateLimiter {
    private final RedisTemplate<String,String> redisTemplate;
    private final DefaultRedisScript<List> tokenBucketScript;

    public RedisRateLimiter(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.tokenBucketScript = new DefaultRedisScript<>();
        this.tokenBucketScript.setLocation(
                new ClassPathResource("lua/token_bucket.lua"));
        this.tokenBucketScript.setResultType(List.class);
    }

    public RateLimitResult check (
            String key,
            long capacity,
            double refillRate
    ){
        long now = Instant.now().toEpochMilli();
        List<Long> result = redisTemplate.execute(
                tokenBucketScript,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(now)
        );
        if (result == null || result.size() < 2) {
            return RateLimitResult.denied();
        }
        boolean allowed = result.get(0) == 1;
        long tokensLeft = result.get(1);

        return new RateLimitResult(allowed,tokensLeft);

    }
}
