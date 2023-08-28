package nandreas.ordermanagement.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequest
{
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email(message = "Invalid format.")
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
