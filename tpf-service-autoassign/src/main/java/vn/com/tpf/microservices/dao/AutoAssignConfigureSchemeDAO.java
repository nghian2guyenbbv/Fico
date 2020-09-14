package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.AutoAssignConfigure;
import vn.com.tpf.microservices.models.AutoAssignScheme;
import vn.com.tpf.microservices.models.Vendor;

import java.util.List;


@Repository
@Transactional
public interface AutoAssignConfigureSchemeDAO extends CrudRepository<AutoAssignScheme, Integer> {

    AutoAssignScheme findAutoAssignSchemeById(int id);

    List<AutoAssignScheme> findAll();
}
