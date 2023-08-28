package nandreas.ordermanagement.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import nandreas.ordermanagement.model.User;
import nandreas.ordermanagement.repository.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver
{
    private UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception
    {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = servletRequest.getHeader("X-API-TOKEN");
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access. The 'X-API-TOKEN' field in the request headers is required."
            );
        }

        User user = this.userRepository.findFirstByToken(token);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access. Please make sure to include the valid 'X-API-TOKEN' in the request headers."
            );
        }

        LocalDateTime tokenExpiredAt = user.getTokenExpiredAt().toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));
        if (tokenExpiredAt.isBefore(currentTime)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access. Token expiration time has passed, please acquire a new token."
            );
        }

        return user;
    }
}
