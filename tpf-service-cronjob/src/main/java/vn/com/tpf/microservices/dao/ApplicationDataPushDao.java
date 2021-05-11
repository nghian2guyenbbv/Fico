package vn.com.tpf.microservices.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.ApplicationDataPushModel;
import vn.com.tpf.microservices.models.function.ResponseFunctionModel;

import java.util.List;

@Transactional
@Repository
public interface ApplicationDataPushDao extends JpaRepository<ApplicationDataPushModel, String> {
    @Query(value = "SELECT * from eform.cron_sync_masterdata()", nativeQuery = true)
    ResponseFunctionModel cronSyncMasterData();

    @Query(value = "SELECT * from eform.syncappstatus_getlist()", nativeQuery = true)
    List<ApplicationDataPushModel> getListSyncDataPush();
}
