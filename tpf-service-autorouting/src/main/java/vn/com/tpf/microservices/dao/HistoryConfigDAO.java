package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.HistoryConfig;

public interface HistoryConfigDAO extends JpaRepository<HistoryConfig, Long> {
}
