package nandreas.ordermanagement.service;

import lombok.AllArgsConstructor;
import nandreas.ordermanagement.dto.user.request.LoginUserRequest;
import nandreas.ordermanagement.dto.user.request.RegisterUserRequest;
import nandreas.ordermanagement.dto.user.response.TokenResponse;
import nandreas.ordermanagement.model.User;
import nandreas.ordermanagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService
{
    private ValidationService validationService;

    private UserRepository userRepository;

    public void register(RegisterUserRequest registerUserRequest)
    {
        this.validationService.validate(registerUserRequest);

        if (this.userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered.");
        }

        User user = new User();
        user.setName(registerUserRequest.getName());
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(BCrypt.hashpw(registerUserRequest.getPassword(), BCrypt.gensalt()));
        user.setIsAdmin(0);

        this.userRepository.save(user);
    }

    public TokenResponse login(LoginUserRequest loginUserRequest)
    {
        this.validationService.validate(loginUserRequest);

        User user = this.userRepository.findFirstByEmail(loginUserRequest.getEmail());
        if (user == null || !BCrypt.checkpw(loginUserRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password is invalid.");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(this.nextDays(30));
        this.userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .tokenExpiredAt(user.getTokenExpiredAt())
                .build();
    }

    public void logout(User user)
    {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        this.userRepository.save(user);
    }

    private Timestamp nextDays(int days)
    {
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));

        return Timestamp.valueOf(currentTime.plus(days, ChronoUnit.DAYS));
    }
}
