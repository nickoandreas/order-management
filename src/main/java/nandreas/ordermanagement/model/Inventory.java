package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventories")
public class Inventory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "qty_total", columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private Integer qtyTotal;

    @Column(name = "qty_reserved", columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private Integer qtyReserved;

    @Column(name = "qty_saleable", columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private Integer qtySaleable;

    @OneToOne
    @JoinColumn(name = "sku",
            referencedColumnName = "sku",
            foreignKey = @ForeignKey(name = "fk_inventories_products_sku"),
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
