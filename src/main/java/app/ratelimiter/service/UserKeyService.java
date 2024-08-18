package app.ratelimiter.service;

import jakarta.servlet.http.HttpServletRequest;

public interface UserKeyService {
    String getUserKey(HttpServletRequest request);
}
