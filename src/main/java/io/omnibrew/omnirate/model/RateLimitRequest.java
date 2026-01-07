package io.omnibrew.omnirate.model;

public class RateLimitRequest {

    private String key;
    private long capacity;
    private double refillRate;

    public RateLimitRequest(){}

    public RateLimitRequest(String key, long capacity, double refillRate){
        this.key = key;
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getCapacity() {
        return capacity;
    }
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
    public double getRefillRate() {
        return refillRate;
    }
    public void setRefillRate(double refillRate) {
        this.refillRate = refillRate;
    }
}
