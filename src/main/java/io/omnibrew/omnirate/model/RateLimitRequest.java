package io.omnibrew.omnirate.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class RateLimitRequest {

    @NotBlank
    private String key;
     @Positive
    private long capacity;
     @Positive
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
