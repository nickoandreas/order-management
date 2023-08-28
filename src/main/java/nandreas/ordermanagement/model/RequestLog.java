package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_logs", indexes = {
        @Index(name = "request_logs_ip_unique", columnList = "ip", unique = true)
})
public class RequestLog
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String ip;
    
    @Column(name = "created_at", columnDefinition = "TIMESTAMP(0)", nullable = false)
    private Timestamp createdAt;

    @Column(name = "request_count", columnDefinition = "INT UNSIGNED DEFAULT 0", nullable = false)
    private Integer requestCount;
}
