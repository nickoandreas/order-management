package nandreas.ordermanagement.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListRequest
{
    private Integer pageSize;

    private Integer currentPage;
}
