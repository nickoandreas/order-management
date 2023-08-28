package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String sku;

    @Column(name = "qty_ordered", columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer qtyOrdered;

    @Column(columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private BigDecimal price;

    @Column(name = "raw_total", columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private BigDecimal rawTotal;

    @ManyToOne
    @JoinColumn(name = "order_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_items_orders_order_id"),
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_items_products_product_id"),
            nullable = false)
    private Product product;
}
