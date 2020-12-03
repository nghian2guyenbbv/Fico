package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.AutoAssignConfigure;
import vn.com.tpf.microservices.models.AutoAssignConfigureApplication;

import java.util.List;

@Transactional
@Repository
public interface AutoAssignConfigureApplicationDAO extends JpaRepository<AutoAssignConfigureApplication, String> {
    AutoAssignConfigureApplication findAutoAssignConfigureApplicationByReferenceId(String referenceId);
    boolean existsAutoAssignConfigureApplicationByReferenceId(String referenceId);
}
