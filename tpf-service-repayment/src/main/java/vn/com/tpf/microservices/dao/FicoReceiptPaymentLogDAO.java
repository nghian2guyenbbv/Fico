package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoReceiptPayment;
import vn.com.tpf.microservices.models.FicoReceiptPaymentLog;

import java.util.List;

@Transactional
public interface FicoReceiptPaymentLogDAO extends CrudRepository<FicoReceiptPaymentLog, Long> {
    FicoReceiptPaymentLog findById(String id);

    @Query(value = "SELECT * FROM payoo.getlistretry(?1)",nativeQuery = true)
    List<FicoReceiptPaymentLog> getListRetry(int status);
}
