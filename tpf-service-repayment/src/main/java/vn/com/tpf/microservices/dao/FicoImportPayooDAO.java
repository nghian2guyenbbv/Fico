package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.tpf.microservices.models.FicoPayooImp;

@Repository
public interface FicoImportPayooDAO extends JpaRepository<FicoPayooImp, String> {

}

