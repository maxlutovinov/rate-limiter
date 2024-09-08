package app.ratelimiter.service.impl;

import app.ratelimiter.model.Bucket;
import app.ratelimiter.service.RateLimiter;
import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenBucketRateLimiter implements RateLimiter {
    private final long rate;
    private final long period;
    private final Clock clock;
    private final Map<String, Bucket> userTokenBucket = new ConcurrentHashMap<>();

    /**
     * Constructs a TokenBucketRateLimiter with the specified parameters.
     *
     * @param rate   The maximum number of allowed tokens per period.
     * @param period The period in milliseconds for the tokens refills.
     */
    @Autowired
    public TokenBucketRateLimiter(@Value("${rate}") long rate,
                                  @Value("${period}") long period) {
        this.rate = rate;
        this.period = period;
        clock = Clock.systemUTC();
    }

    /**
     * Constructs a TokenBucketRateLimiter with the specified parameters. Used for testing.
     *
     * @param rate   The maximum number of allowed tokens per period.
     * @param period The period for the tokens refill.
     * @param clock  The clock instance to use for timing.
     */
    public TokenBucketRateLimiter(long rate, long period, Clock clock) {
        this.rate = rate;
        this.period = period;
        this.clock = clock;
    }

    /**
     * Attempts to consume a token from the bucket for the specific user key.
     *
     * @param userKey The key of the user making the request to consume a token.
     * @return true if a token was successfully consumed; false otherwise.
     */
    @Override
    public boolean tryConsume(String userKey) {
        // Retrieve the bucket for the existing user or initialize an empty bucket for a new user
        Bucket bucket = userTokenBucket.computeIfAbsent(userKey,
                k -> new Bucket(clock.millis(), new AtomicLong(rate)));

        // Refill the bucket with tokens based on elapsed time since the last refill.
        refill(bucket);

        // Consume a single token from the bucket, if available.
        if (bucket.getTokenCount().get() > 0) {
            bucket.getTokenCount().decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * Regenerates tokens in the bucket for the fixed period, waiting until
     * the entire period passed before regenerating the full rate of tokens per period.
     *
     * @param bucket The user bucket with the timestamp of the last token refill
     *               and the available number of tokens.
     */
    private void refill(Bucket bucket) {
        long currentTime = clock.millis();
        long elapsedPeriods = (currentTime - bucket.getRefillTimestamp()) / period;
        long tokensToRefill = Math.min(rate,
                bucket.getTokenCount().addAndGet(elapsedPeriods * rate));

        if (tokensToRefill > 0) {
            bucket.getTokenCount().set(tokensToRefill);
            bucket.setRefillTimestamp(bucket.getRefillTimestamp() + elapsedPeriods * period);
        }
    }
}
