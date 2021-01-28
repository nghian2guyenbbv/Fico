package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.tpf.microservices.models.AllocationPendingCode;

public interface AllocationPendingCodeDao extends JpaRepository<AllocationPendingCode, Long>, JpaSpecificationExecutor {
}
