package nandreas.ordermanagement.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nandreas.ordermanagement.annotation.RateLimit;
import nandreas.ordermanagement.model.RequestLog;
import nandreas.ordermanagement.repository.RequestLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@AllArgsConstructor
@Component
public class RateLimitInterceptor implements HandlerInterceptor
{
    private RequestLogRepository requestLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod handlerMethod) {
            RateLimit methodRateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
            RateLimit classRateLimit = handlerMethod.getBeanType().getAnnotation(RateLimit.class);

            RateLimit rateLimitAnnotation = methodRateLimit != null ? methodRateLimit : classRateLimit;

            if (rateLimitAnnotation != null) {
                int limitRequest = rateLimitAnnotation.value();
                int periodRequest = rateLimitAnnotation.period();

                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) {
                    ip = request.getRemoteAddr();
                }

                RequestLog requestLog = this.requestLogRepository.findFirstByIp(ip);
                if (requestLog == null) {
                    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

                    requestLog = new RequestLog();
                    requestLog.setCreatedAt(createdAt);
                    requestLog.setIp(ip);
                    requestLog.setRequestCount(1);
                } else {
                    LocalDateTime currentTimeRequest = LocalDateTime.now(ZoneId.of("UTC"));
                    LocalDateTime expiredTime = requestLog.getCreatedAt().toLocalDateTime().plusSeconds(periodRequest);

                    if (requestLog.getRequestCount() >= limitRequest
                            && currentTimeRequest.isBefore(expiredTime)) {

                        throw new ResponseStatusException(
                                HttpStatus.TOO_MANY_REQUESTS,
                                "The rate limit for requests has been exceeded. Please try again later."
                        );

                    } else if (currentTimeRequest.isAfter(expiredTime)) {
                        Timestamp reCreatedAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

                        requestLog.setCreatedAt(reCreatedAt);
                        requestLog.setRequestCount(1);
                    } else {
                        requestLog.setRequestCount(requestLog.getRequestCount() + 1);
                    }

                }

                this.requestLogRepository.save(requestLog);
            }
        }

        return true;
    }
}
