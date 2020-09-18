package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.AssignConfig;
import vn.com.tpf.microservices.models.HistoryAllocation;

import java.util.List;

public interface AssignConfigDAO extends JpaRepository<AssignConfig, Long> {
    List<AssignConfig> findAllByOrderByStageName();
}
