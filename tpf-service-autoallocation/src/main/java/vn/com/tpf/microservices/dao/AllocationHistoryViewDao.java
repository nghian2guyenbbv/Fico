package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.tpf.microservices.models.AllocationHistoryView;



public interface AllocationHistoryViewDao extends JpaRepository<AllocationHistoryView, Long>, JpaSpecificationExecutor {

}

