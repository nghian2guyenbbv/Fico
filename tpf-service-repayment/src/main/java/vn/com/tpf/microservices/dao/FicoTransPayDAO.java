package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoTransPay;


@Transactional
public interface FicoTransPayDAO extends CrudRepository<FicoTransPay, Long> {
    FicoTransPay findByTransactionId(String id);


}
