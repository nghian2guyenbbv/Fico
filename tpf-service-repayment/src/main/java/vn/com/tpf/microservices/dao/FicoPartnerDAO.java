package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoPartner;


@Transactional
public interface FicoPartnerDAO extends CrudRepository<FicoPartner, Integer> {
    FicoPartner findById(int id);
}
