package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tpf.microservices.models.FicoTransCancel;

@Repository
public interface FicoTransCancelDAO extends JpaRepository<FicoTransCancel, Long> {

    FicoTransCancel findByTransactionCancelIdAndApproveStatus(String transactionCancelId, int approveStatus);
}
