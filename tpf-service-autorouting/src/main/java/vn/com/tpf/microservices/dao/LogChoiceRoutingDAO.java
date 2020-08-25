package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.LogChoiceRouting;

public interface LogChoiceRoutingDAO extends JpaRepository<LogChoiceRouting, Long> {

    LogChoiceRouting findByIdLog(int idLog);
}
