package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", indexes = {
        @Index(name = "products_sku_unique", columnList = "sku", unique = true),
        @Index(name = "products_name_unique", columnList = "name", unique = true)
})
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String sku;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "DECIMAL(20, 2) DEFAULT 0.00", nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;
}
