package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    boolean existsByEmail(String email);

    User findFirstByEmail(String email);

    User findFirstByToken(String token);
}
