package nandreas.ordermanagement.dto.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemListResponse
{
    private Integer id;

    @JsonProperty("product_id")
    private Integer productId;

    private String name;

    private String sku;

    private BigDecimal price;

    @JsonProperty("qty_ordered")
    private Integer qtyOrdered;

    @JsonProperty("raw_total")
    private BigDecimal rawTotal;

    @JsonProperty("image_url")
    private String imageUrl;
}
