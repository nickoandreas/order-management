package nandreas.ordermanagement.controller;

import lombok.AllArgsConstructor;
import nandreas.ordermanagement.annotation.RateLimit;
import nandreas.ordermanagement.dto.user.request.LoginUserRequest;
import nandreas.ordermanagement.dto.user.request.RegisterUserRequest;
import nandreas.ordermanagement.dto.ResponseStatus;
import nandreas.ordermanagement.dto.WebResponse;
import nandreas.ordermanagement.dto.user.response.TokenResponse;
import nandreas.ordermanagement.model.User;
import nandreas.ordermanagement.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RateLimit
@AllArgsConstructor
@RestController
public class UserController
{
    private UserService userService;

    @PostMapping(
            path = "/api/users/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest registerUserRequest)
    {
        this.userService.register(registerUserRequest);

        return WebResponse.<String>builder()
                .status(ResponseStatus.SUCCESS.toLowerCase())
                .message("Registration successful.")
                .build();
    }

    @PostMapping(
            path = "/api/users/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest loginUserRequest)
    {
        TokenResponse tokenResponse = this.userService.login(loginUserRequest);

        return WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .status(ResponseStatus.SUCCESS.toLowerCase())
                .build();
    }

    @PostMapping(
            path = "/api/users/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user)
    {
        this.userService.logout(user);

        return WebResponse.<String>builder()
                .status(ResponseStatus.SUCCESS.toLowerCase())
                .message("Logout successful.")
                .build();
    }
}
