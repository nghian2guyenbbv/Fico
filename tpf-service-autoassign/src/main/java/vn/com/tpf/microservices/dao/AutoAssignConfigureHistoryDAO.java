package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.AutoAssignConfigure;
import vn.com.tpf.microservices.models.AutoAssignConfigureHistory;

import java.util.List;

@Transactional
@Repository
public interface AutoAssignConfigureHistoryDAO extends JpaRepository<AutoAssignConfigureHistory, String> {


    @Query(value = "SELECT * " +
            "FROM auto_assign.autoassign_configure_history", nativeQuery = true)
    List<AutoAssignConfigureHistory> getHistory(Pageable paging);

    @Query(value = "select count(*) from auto_assign.autoassign_configure_history", nativeQuery = true)
    int totalRow();
}

