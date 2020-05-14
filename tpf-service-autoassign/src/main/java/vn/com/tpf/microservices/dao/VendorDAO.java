package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.Vendor;

import java.util.List;


@Transactional
public interface VendorDAO extends CrudRepository<Vendor, Long> {
    List<Vendor> findAll();
    Vendor findVendorById(Long id);
}
