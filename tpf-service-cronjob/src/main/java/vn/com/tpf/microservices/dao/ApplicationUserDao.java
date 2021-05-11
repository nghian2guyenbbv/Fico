package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.eform.ApplicationEformSyncModel;
import vn.com.tpf.microservices.models.eform.ApplicationUser;

import java.util.List;

@Transactional
@Repository
public interface ApplicationUserDao extends JpaRepository<ApplicationUser, String> {
    @Query(value = "select * from eform.applicationuser_syncstatus()", nativeQuery = true)
    List<ApplicationUser> getListSyncStatus();

    @Query(value = "select * from eform.application_eform_sync_data()", nativeQuery = true)
    List<ApplicationEformSyncModel> getListCollectSyncStatus();
}
