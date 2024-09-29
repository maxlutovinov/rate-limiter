package app.ratelimiter.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TokenBucketRateLimiterTest {
    private static final String FIRST_USER_IP = "FIRST_USER_IP";
    private static final String SECOND_USER_IP = "SECOND_USER_IP";
    private static final long CACHE_CLEAR_INTERVAL = 10000;
    private static final long RATE = 2;
    private static final long PERIOD = 5000;

    @Test
    void tryConsume_OneUser() {
        Clock clock = mock(Clock.class);
        Mockito.when(clock.millis()).thenReturn(0L, 0L, 1000L, 4999L, 5000L, 5000L, 5002L,
                15000L, 15001L, 15002L);

        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(RATE, PERIOD,
                CACHE_CLEAR_INTERVAL, clock);

        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertFalse(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertFalse(rateLimiter.tryConsume(FIRST_USER_IP));

        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertFalse(rateLimiter.tryConsume(FIRST_USER_IP));
    }

    @Test
    void tryConsume_TwoUsers() {
        Clock clock = mock(Clock.class);
        Mockito.when(clock.millis()).thenReturn(0L, 0L, 1000L, 4995L, 4995L, 4999L, 5000L, 5000L,
                10000L, 15000L,
                15001L, 15001L, 15002L, 15002L, 15003L, 15004L, 20000L, 20000L);

        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(RATE, PERIOD,
                CACHE_CLEAR_INTERVAL, clock);

        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(SECOND_USER_IP));
        assertFalse(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(SECOND_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));

        assertEquals(2, rateLimiter.clearUserCache().size());
        assertEquals(0, rateLimiter.clearUserCache().size());

        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(SECOND_USER_IP));
        assertTrue(rateLimiter.tryConsume(FIRST_USER_IP));
        assertTrue(rateLimiter.tryConsume(SECOND_USER_IP));
        assertFalse(rateLimiter.tryConsume(FIRST_USER_IP));
        assertFalse(rateLimiter.tryConsume(SECOND_USER_IP));
    }
}
