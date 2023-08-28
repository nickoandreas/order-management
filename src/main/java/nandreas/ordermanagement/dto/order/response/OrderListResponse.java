package nandreas.ordermanagement.dto.order.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponse
{
    private Integer id;

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("ordered_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp orderedAt;

    private String status;

    @JsonProperty("grand_total")
    private BigDecimal grandTotal;

    private List<OrderItemListResponse> items;
}
