package nandreas.ordermanagement.dto.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest
{
    @NotBlank
    @Size(max = 100)
    private String sku;

    @NotBlank
    @Positive(message = "Invalid quantity.")
    private Integer qty;
}
