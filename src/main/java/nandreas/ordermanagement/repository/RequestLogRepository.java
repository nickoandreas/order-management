package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Integer>
{
    RequestLog findFirstByIp(String ip);
}
