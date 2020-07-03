package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoReceiptPayment;
import vn.com.tpf.microservices.models.FicoReceiptPaymentLog;

@Transactional
public interface FicoReceiptPaymentLogDAO extends CrudRepository<FicoReceiptPaymentLog, Long> {
    FicoReceiptPaymentLog findById(String id);
}
