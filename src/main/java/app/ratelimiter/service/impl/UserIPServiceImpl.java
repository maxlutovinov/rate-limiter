package app.ratelimiter.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import app.ratelimiter.service.UserKeyService;
import org.springframework.stereotype.Service;

@Service
public class UserIPServiceImpl implements UserKeyService {
    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    /**
     * Retrieve client IP address from HTTP request.
     *
     * @param request The client HTTP request.
     * @return The client IP address as a String.
     */
    @Override
    public String getUserKey(HttpServletRequest request) {
        String remoteAddress = "";
        if (request != null) {
            remoteAddress = request.getHeader(X_FORWARDED_FOR);
            if (remoteAddress == null || remoteAddress.isEmpty()) {
                remoteAddress = request.getRemoteAddr();
            }
        }
        return remoteAddress;
    }
}
