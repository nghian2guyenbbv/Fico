package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoReceiptPayment;
import vn.com.tpf.microservices.models.FicoTransPay;


@Transactional
public interface FicoReceiptPaymentDAO extends CrudRepository<FicoReceiptPayment, Long> {
    FicoReceiptPayment findById(String id);
}
