package nandreas.ordermanagement.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nandreas.ordermanagement.dto.order.request.ItemRequest;
import nandreas.ordermanagement.dto.order.request.CreateOrderRequest;
import nandreas.ordermanagement.dto.order.request.OrderListRequest;
import nandreas.ordermanagement.dto.order.response.OrderItemListResponse;
import nandreas.ordermanagement.dto.order.response.OrderListResponse;
import nandreas.ordermanagement.dto.order.response.OrderSummaryResponse;
import nandreas.ordermanagement.model.*;
import nandreas.ordermanagement.repository.InventoryRepository;
import nandreas.ordermanagement.repository.OrderItemRepository;
import nandreas.ordermanagement.repository.OrderRepository;
import nandreas.ordermanagement.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService
{
    private ValidationService validationService;

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    private InventoryRepository inventoryRepository;

    private OrderItemRepository orderItemRepository;

    private final Environment environment;

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest, User user)
    {
        this.validationService.validate(createOrderRequest);
        Order order = this.processOrder(createOrderRequest, user);

        return this.placeOrder(order, createOrderRequest.getItems());
    }

    public Boolean updateOrder(String orderNumber)
    {
        // For this test, only updates from 'pending' to 'new' order status are accepted.
        Order order = this.orderRepository.findFirstByOrderNumber(orderNumber);
        if (Objects.isNull(order)) {
            log.info(String.format("Order with number %s not found.", orderNumber));
            return false;
        } else if (!Objects.equals(order.getStatus(), OrderStatus.PENDING.toLowercase())) {
            log.info(String.format(
                    "An order with the status '%s' cannot be updated to '%s'.",
                    order.getStatus(), OrderStatus.NEW.toLowercase()
            ));

            return false;
        }

        order.setStatus(OrderStatus.NEW.toLowercase());
        this.orderRepository.save(order);

        return true;
    }

    public Page<OrderListResponse> getList(User user, OrderListRequest orderListRequest)
    {
        Specification<Order> specification = user.getIsAdmin() != 1 ?
                (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.equal(root.get("user"), user));

                    return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
                } : null;

        Pageable pageable = PageRequest.of(orderListRequest.getCurrentPage(), orderListRequest.getPageSize());

        Page<Order> orders = specification != null ?
                this.orderRepository.findAll(specification, pageable)
                : this.orderRepository.findAll(pageable);

        List<OrderListResponse> orderListResponses = orders.getContent().stream()
                .map(this::toOrderListResponse)
                .toList();

        return new PageImpl<>(orderListResponses, pageable, orders.getTotalElements());
    }

    public OrderSummaryResponse getOrderSummary(User user)
    {
        if (user.getIsAdmin() != 1) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access. Please use an admin account.");
        }

        try {
            String filename = this.generateAllOrderToCsv();
            String baseUrl = this.environment.getProperty("application.baseurl");
            String csvUrl = String.format("%s/temp/%s", baseUrl, filename);

            return OrderSummaryResponse.builder()
                    .url(csvUrl)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again.");
        }
    }

    private String generateAllOrderToCsv() throws IOException
    {
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String filename = String.format("order_%s.csv", fileDateFormat.format(new Date()));
        String csvOutputPath = String.format("assets/temp/%s", filename);
        Path outputPath = Paths.get(csvOutputPath);

        SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> orderSummaries = this.orderRepository.getOrderSummary();
        try (FileWriter fileWriter = new FileWriter(outputPath.toFile())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("Order Number", "Customer Name", "Order Date", "Grand Total", "Status")
                    .build();
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat);

            for (Map<String, Object> orderSummary : orderSummaries) {
                csvPrinter.printRecord(
                        orderSummary.get("order_number"),
                        orderSummary.get("customer_name"),
                        orderDateFormat.format(orderSummary.get("order_date")),
                        orderSummary.get("grand_total"),
                        orderSummary.get("status")
                );
            }

            csvPrinter.flush();
        }

        return filename;
    }

    private OrderListResponse toOrderListResponse(Order order)
    {
        List<OrderItemListResponse> orderItemListResponse = new ArrayList<>();
        for (OrderItem item : order.getItemList()) {
            Product product = item.getProduct();
            String baseUrl = this.environment.getProperty("application.baseurl");
            String imageUrl = (product.getImage() != null && !product.getImage().isEmpty()) ?
                    String.format("%s/assets/image/%s", baseUrl, product.getImage()) : "";

            orderItemListResponse.add(OrderItemListResponse.builder()
                    .id(item.getId())
                    .productId(product.getId())
                    .name(item.getName())
                    .sku(item.getSku())
                    .price(item.getPrice())
                    .qtyOrdered(item.getQtyOrdered())
                    .rawTotal(item.getRawTotal())
                    .imageUrl(imageUrl)
                    .build());
        }

        return OrderListResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderedAt(order.getOrderedAt())
                .status(order.getStatus())
                .grandTotal(order.getGrandTotal())
                .items(orderItemListResponse)
                .build();
    }

    public Order placeOrder(Order order, List<ItemRequest> items)
    {
        List<String> skuListRequest = items.stream()
                .map(ItemRequest::getSku)
                .distinct()
                .toList();

        List<Product> products = this.productRepository.findBySkuIn(skuListRequest);

        if (products.size() != skuListRequest.size()) {
            List<String> skuListFound = products.stream()
                    .map(Product::getSku)
                    .toList();

            List<String> skuListNotFound = new ArrayList<>(skuListRequest);
            skuListNotFound.removeAll(skuListFound);

            String joinedSkuNotFound = String.join(",", skuListNotFound);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid SKU : " + joinedSkuNotFound
            );
        }

        Map<String, Product> skuToProductMap = products.stream()
                .collect(Collectors.toMap(Product::getSku, product -> product));

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (ItemRequest item : items) {
            Inventory inventory = this.inventoryRepository.findFirstByProductSku(item.getSku());
            if (inventory == null || item.getQty() > inventory.getQtySaleable()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Allocation Not Found : " + item.getSku() + "."
                );
            }

            inventory.setQtyReserved(inventory.getQtyReserved() + item.getQty());
            inventory.setQtySaleable(inventory.getQtySaleable() - item.getQty());
            this.inventoryRepository.save(inventory);

            OrderItem orderItem = this.orderItemRepository.findFirstByOrderAndSku(order, item.getSku());
            Product product = skuToProductMap.get(item.getSku());
            if (orderItem == null) {
                orderItem = new OrderItem();

                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setName(product.getName());
                orderItem.setSku(product.getSku());
                orderItem.setPrice(product.getPrice());
                orderItem.setQtyOrdered(item.getQty());
                orderItem.setRawTotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQty())));
            } else {
                BigDecimal currentRawTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
                orderItem.setQtyOrdered(orderItem.getQtyOrdered() + item.getQty());
                orderItem.setRawTotal(orderItem.getRawTotal().add(currentRawTotal));
            }

            grandTotal = grandTotal.add(orderItem.getRawTotal());

            this.orderItemRepository.save(orderItem);
        }

        order.setGrandTotal(grandTotal);
        this.orderRepository.save(order);

        return order;
    }

    private Order processOrder(CreateOrderRequest createOrderRequest, User user)
    {
        if (this.orderRepository.existsByOrderNumber(createOrderRequest.getOrderNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order Number " + createOrderRequest.getOrderNumber() + " is already exists."
            );
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(createOrderRequest.getOrderNumber());
        order.setOrderedAt(Timestamp.valueOf(createOrderRequest.getOrderedAt()));
        order.setStatus(OrderStatus.PENDING.toLowercase());
        this.orderRepository.save(order);

        return order;
    }
}
