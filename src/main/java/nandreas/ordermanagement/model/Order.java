package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "orders_order_number_unique", columnList = "order_number", unique = true)
})
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_number", length = 100, nullable = false)
    private String orderNumber;

    @Column(name = "ordered_at", columnDefinition = "TIMESTAMP(0)")
    private Timestamp orderedAt;

    @Column(name = "grand_total", columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00")
    private BigDecimal grandTotal;

    @Column(length = 50, nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_orders_users_user_id"),
            nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> itemList;
}
