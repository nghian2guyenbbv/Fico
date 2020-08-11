package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoTransPay;
import vn.com.tpf.microservices.models.FicoTransPaySettle;

@Transactional
public interface FicoTransPaySettleDAO extends CrudRepository<FicoTransPaySettle, Long> {
    FicoTransPaySettle findByTransactionId(String id);
}
