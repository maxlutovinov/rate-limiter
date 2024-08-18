package app.ratelimiter.controller;

import jakarta.servlet.http.HttpServletRequest;
import app.ratelimiter.service.RateLimiter;
import app.ratelimiter.service.UserKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RateLimitController {
    private final UserKeyService userKeyService;
    private final RateLimiter rateLimiter;

    public RateLimitController(UserKeyService userKeyService, RateLimiter rateLimiter) {
        this.userKeyService = userKeyService;
        this.rateLimiter = rateLimiter;
    }

    /**
     * Endpoint with rate-limited access per period for a unique IP address.
     *
     * @param request The client HTTP request.
     * @return The successful access within the rate threshold for the period specified in
     * the application properties and a warning if the number of requests exceeds the rate.
     */
    @GetMapping("/")
    public String limit(HttpServletRequest request) {
        if (rateLimiter.tryConsume(userKeyService.getUserKey(request))) {
            return "Success";
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
    }
}
