package app.ratelimiter.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import app.ratelimiter.service.RateLimiter;
import app.ratelimiter.service.UserKeyService;
import java.io.IOException;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterFilter implements Filter {
    private final UserKeyService userKeyService;
    private final RateLimiter rateLimiter;
    private Set<String> rateLimitedUrls;

    public RateLimiterFilter(UserKeyService userKeyService, RateLimiter rateLimiter) {
        this.userKeyService = userKeyService;
        this.rateLimiter = rateLimiter;
    }

    /**
     * Sets the rate-limited URLs exactly once after instantiating the filter.
     *
     * @param filterConfig The configuration information associated with the filter instance
     *                     being initialized.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        rateLimitedUrls = Set.of("/");
    }

    /**
     * Filters access to specific endpoints by limiting the number of requests per period from a
     * unique IP address to the rate threshold for the period specified in the application
     * properties.
     *
     * @param request  The request to process.
     * @param response The response associated with the request.
     * @param chain    Provides access to endpoint controllers passing the request and response
     *                 for further processing or send an error response if the number of requests
     *                 exceeds the rate.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (rateLimitedUrls.contains(req.getServletPath())) {
            if (rateLimiter.tryConsume(userKeyService.getUserKey(req))) {
                chain.doFilter(request, response);
                return;
            }
            ((HttpServletResponse) response).sendError(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        chain.doFilter(request, response);
    }
}
