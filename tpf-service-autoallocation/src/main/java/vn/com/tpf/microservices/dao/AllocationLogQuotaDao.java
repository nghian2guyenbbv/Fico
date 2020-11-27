package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.tpf.microservices.models.AllocationLogQuota;

public interface AllocationLogQuotaDao extends JpaRepository<AllocationLogQuota, Long>, JpaSpecificationExecutor {
}
