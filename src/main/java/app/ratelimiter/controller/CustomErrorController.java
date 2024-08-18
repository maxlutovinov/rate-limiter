package app.ratelimiter.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {
    @Value("${period}")
    long period;

    /**
     * Custom error handling.
     *
     * @param request The HTTP request.
     * @return The custom response depending on HTTP status code.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
                return "Too many requests per " + period / 1000 + " sec.";
            }
        }
        return "Something went wrong!";
    }
}
