package nandreas.ordermanagement.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nandreas.ordermanagement.annotation.RateLimit;
import nandreas.ordermanagement.dto.PageInfoResponse;
import nandreas.ordermanagement.dto.ResponseStatus;
import nandreas.ordermanagement.dto.WebResponse;
import nandreas.ordermanagement.dto.order.request.CreateOrderRequest;
import nandreas.ordermanagement.dto.order.request.OrderListRequest;
import nandreas.ordermanagement.dto.order.response.OrderListResponse;
import nandreas.ordermanagement.dto.order.response.OrderSummaryResponse;
import nandreas.ordermanagement.model.Order;
import nandreas.ordermanagement.model.User;
import nandreas.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RateLimit
@AllArgsConstructor
@RestController
public class OrderController
{
    private OrderService orderService;

    @PostMapping(
            path = "/api/order",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> create(@RequestBody CreateOrderRequest createOrderRequest, User user)
    {
        Order order = this.orderService.createOrder(createOrderRequest, user);

        return WebResponse.<String>builder()
                .status(ResponseStatus.SUCCESS.toLowerCase())
                .message(String.format("Order with number %s successfully created.", order.getOrderNumber()))
                .build();
    }

    @GetMapping(
            path = "/api/order",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OrderListResponse>> get(User user,
                                                    @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "current_page", required = false, defaultValue = "0") Integer currentPage)
    {
        OrderListRequest orderListRequest = OrderListRequest.builder()
                .pageSize(pageSize)
                .currentPage(currentPage)
                .build();

        Page<OrderListResponse> orderListResponse = this.orderService.getList(user, orderListRequest);

        return WebResponse.<List<OrderListResponse>>builder()
                .data(orderListResponse.getContent())
                .totalCount(orderListResponse.getTotalElements())
                .pageInfo(PageInfoResponse.builder()
                        .currentPage(orderListResponse.getNumber())
                        .pageSize(orderListResponse.getSize())
                        .totalPages(orderListResponse.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/api/order/export",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderSummaryResponse> getOrderSummary(User user)
    {
        OrderSummaryResponse orderSummaryResponse = this.orderService.getOrderSummary(user);

        return WebResponse.<OrderSummaryResponse>builder()
                .data(orderSummaryResponse)
                .status(ResponseStatus.SUCCESS.toLowerCase())
                .build();
    }
}
