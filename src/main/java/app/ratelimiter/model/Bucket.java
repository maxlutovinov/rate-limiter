package app.ratelimiter.model;

import lombok.Data;

@Data
public class Bucket {
    private long refillTimestamp;
    private long tokenCount;

    /**
     * Constructs a Bucket with the specified initial state.
     *
     * @param refillTimestamp The timestamp of the last refill.
     * @param tokenCount      The initial number of tokens in the bucket.
     */
    public Bucket(long refillTimestamp, long tokenCount) {
        this.refillTimestamp = refillTimestamp;
        this.tokenCount = tokenCount;
    }
}
