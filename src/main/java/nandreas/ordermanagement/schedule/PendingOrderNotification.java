package nandreas.ordermanagement.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nandreas.ordermanagement.dto.EmailAttribute;
import nandreas.ordermanagement.model.Order;
import nandreas.ordermanagement.model.OrderItem;
import nandreas.ordermanagement.model.OrderStatus;
import nandreas.ordermanagement.model.User;
import nandreas.ordermanagement.repository.OrderRepository;
import nandreas.ordermanagement.service.EmailService;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class PendingOrderNotification
{
    private EmailService emailService;

    private OrderRepository orderRepository;

    private Environment environment;

    @Scheduled(cron = "0 00 00 * * *")
    public void sendPendingOrderEmailNotification()
    {
        log.info("Send Email Pending Order Notification...");

        SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String baseUrl = this.environment.getProperty("application.baseurl");

        List<Order> pendingOrders = this.orderRepository.findOrderByStatus(OrderStatus.PENDING.toLowercase());

        for (Order pendingOrder : pendingOrders) {
            User customer = pendingOrder.getUser();
            List<OrderItem> orderItems = pendingOrder.getItemList();

            Context context = new Context();
            context.setVariable("orderNumber", pendingOrder.getOrderNumber());
            context.setVariable("orderDate", orderDateFormat.format(pendingOrder.getOrderedAt()));
            context.setVariable("grandTotal", pendingOrder.getGrandTotal());
            context.setVariable("customerName", customer.getName());
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("orderItems", orderItems);

            EmailAttribute emailAttribute = EmailAttribute.builder()
                    .from("store@gmail.com")
                    .to(customer.getEmail())
                    .subject("Pending Order Notification")
                    .build();

            this.emailService.sendEmail(
                    "pending-order-notification-template",
                    context,
                    emailAttribute
            );
        }
    }
}
