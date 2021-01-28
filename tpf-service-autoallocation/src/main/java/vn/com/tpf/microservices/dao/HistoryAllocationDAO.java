package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.HistoryAllocation;

public interface HistoryAllocationDAO extends JpaRepository<HistoryAllocation, Long> {
}
