package io.omnibrew.omnirate.redis;

public class RateLimitResult {
    private final boolean allowed;
    private final long remainingTokens;

    public RateLimitResult(boolean allowed, long remainingTokens) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
    }

    public static RateLimitResult denied() {
        return new RateLimitResult(false, 0);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRemainingTokens() {
        return remainingTokens;
    }
}
