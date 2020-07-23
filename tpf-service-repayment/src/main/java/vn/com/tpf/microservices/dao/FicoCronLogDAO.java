package vn.com.tpf.microservices.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.FicoCronLogModel;

import javax.persistence.Entity;
import java.util.List;

@Transactional
public interface FicoCronLogDAO extends CrudRepository<FicoCronLogModel, Long> {

    FicoCronLogModel findById(String id);
    List<FicoCronLogModel> findByFileName(String fileName);
}
