package io.omnibrew.omnirate.service;

import io.omnibrew.omnirate.model.RateLimitRequest;
import io.omnibrew.omnirate.model.RateLimitResponse;
import io.omnibrew.omnirate.redis.RateLimitResult;
import io.omnibrew.omnirate.redis.RedisRateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {
    private final RedisRateLimiter redisRateLimiter;

    public RateLimitService(RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;
    }

    public long computeRetryAfterMs(long capacity, double refillRate) {
        if (refillRate <= 0) {
            return -1;
        }
        return (long) (1000 / refillRate);
    }

    public RateLimitResponse checkRateLimit(RateLimitRequest rateLimitRequest) {
        String redisKey = "uniqueKey:" + rateLimitRequest.getKey();
        RateLimitResult rateLimitResult = redisRateLimiter.check(
                redisKey,
                rateLimitRequest.getCapacity(),
                rateLimitRequest.getRefillRate());
        long retryAfterMs = rateLimitResult.isAllowed() ? 0
                : computeRetryAfterMs(
                        rateLimitRequest.getCapacity(),
                        rateLimitRequest.getRefillRate());
        return new RateLimitResponse(
                rateLimitResult.isAllowed(),
                rateLimitResult.getRemainingTokens(),
                retryAfterMs);
    }

}
