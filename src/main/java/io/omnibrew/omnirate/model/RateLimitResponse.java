package io.omnibrew.omnirate.model;

public class RateLimitResponse {
    private boolean allowed;
    private long remainingTokens;
    private long retryAfterMs;

    public RateLimitResponse() {}

    public RateLimitResponse(boolean allowed, long remainingTokens, long retryAfterMs) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.retryAfterMs = retryAfterMs;
    }

    public boolean isAllowed() {
        return allowed;
    }
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
    public long getRemainingTokens() {
        return remainingTokens;
    }
    public void setRemainingTokens(long remainingTokens) {
        this.remainingTokens = remainingTokens;
    }
    public long getRetryAfterMs() {
        return retryAfterMs;
    }
    public void setRetryAfterMs(long retryAfterMs) {
        this.retryAfterMs = retryAfterMs;
    }

}
