package app.ratelimiter.model;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;

@Data
public class Bucket {
    private final AtomicLong tokenCount;
    private volatile long refillTimestamp;

    /**
     * Constructs a Bucket with the specified initial state.
     *
     * @param refillTimestamp The timestamp of the last refill.
     * @param tokenCount      The initial number of tokens in the bucket.
     */
    public Bucket(long refillTimestamp, AtomicLong tokenCount) {
        this.refillTimestamp = refillTimestamp;
        this.tokenCount = tokenCount;
    }
}
