package nandreas.ordermanagement.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAttribute
{
    @Email
    private String from;

    @Email
    private String to;

    private String subject;
}
