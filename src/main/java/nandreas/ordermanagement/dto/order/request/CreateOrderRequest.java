package nandreas.ordermanagement.dto.order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest
{
    @NotBlank
    @Size(max = 100)
    @JsonProperty("order_number")
    private String orderNumber;

    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$",
            message = "Invalid format. Use yyyy-MM-dd HH:mm:ss."
    )
    @NotBlank
    @JsonProperty(value = "ordered_at")
    private String orderedAt;

    private List<ItemRequest> items;
}
