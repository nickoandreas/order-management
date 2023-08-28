package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.Order;
import nandreas.ordermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order>
{
    Boolean existsByOrderNumber(String orderNumber);

    @Query(value = "SELECT o.order_number, o.ordered_at order_date, o.status, o.grand_total, " +
            "u.name customer_name " +
            "FROM orders o " +
            "JOIN users u ON o.user_id = u.id " +
            "ORDER BY order_date" +
            " DESC", nativeQuery = true)
    List<Map<String, Object>> getOrderSummary();

    List<Order> findOrderByStatus(String status);

    Order findFirstByOrderNumber(String orderNumber);
}
