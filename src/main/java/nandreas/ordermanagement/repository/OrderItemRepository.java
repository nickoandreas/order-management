package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.Order;
import nandreas.ordermanagement.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>
{
    OrderItem findFirstByOrderAndSku(Order order, String sku);
}
