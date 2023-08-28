package nandreas.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "users_email_unique", columnList = "email", unique = true),
        @Index(name = "users_token_unique", columnList = "token", unique = true)
})
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100)
    private String token;

    @Column(name = "token_expired_at", columnDefinition = "TIMESTAMP(0)")
    private Timestamp tokenExpiredAt;

    @Column(name = "is_admin", columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Integer isAdmin;
}
