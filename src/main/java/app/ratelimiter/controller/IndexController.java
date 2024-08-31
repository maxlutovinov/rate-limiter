package app.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    /**
     * Endpoint with rate-limited access filtered by RateLimiterFilter.
     *
     * @return The successful access.
     */
    @GetMapping("/")
    public String hello() {
        return "Success";
    }
}
