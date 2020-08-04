package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoTransPay;
import vn.com.tpf.microservices.models.FicoTransPayQueue;

@Transactional
public interface FicoTransPayQueueDAO extends CrudRepository<FicoTransPayQueue, Long> {
    FicoTransPayQueue findByTransactionId(String id);
}
