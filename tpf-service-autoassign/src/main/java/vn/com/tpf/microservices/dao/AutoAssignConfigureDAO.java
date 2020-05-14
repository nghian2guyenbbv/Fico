package vn.com.tpf.microservices.dao;

import org.jboss.logging.annotations.Param;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tpf.microservices.models.AutoAssignConfigure;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Repository
public interface AutoAssignConfigureDAO extends JpaRepository<AutoAssignConfigure, String> {
    @Modifying
    @Query(value = "UPDATE auto_assign.autoassign_configure " +
                    "SET total_quanlity = ?1, quanlity_in_day = ?2, priority = ?3, id_seq = nextval('auto_assign.seq_autoassign_configure'), is_assign = ?4, updated_date = current_date " +
                    "where id = ?5 and id_seq = ?6 ", nativeQuery = true)
    int updateConfigure(long total_quanlity, long quanlity_in_day, long priority, boolean isAssign, long id, long id_seq);

    @Modifying
    @Query(value = "UPDATE auto_assign.autoassign_configure " +
                    "SET actual_total_quanlity = ?1, actual_quanlity_in_day = ?2, id_seq = nextval('auto_assign.seq_autoassign_configure'), updated_date = current_date " +
                    "WHERE id = ?3 and id_seq = ?4 ", nativeQuery = true)
    int updateConfigureApplication(long actual_total_quanlity, long actual_quanlity_in_day, long id, long id_seq);

    AutoAssignConfigure findConfigureById(Long id);

    @Query(value = "SELECT * " +
                    "FROM auto_assign.autoassign_configure " +
                    "WHERE to_char(created_date, 'yyyy-MM-dd')  = ?1 " +
                    "order by priority ", nativeQuery = true)
    List<AutoAssignConfigure> findConfigureByCreatedDate(String date);

    @Query(value = "select * from auto_assign.getvendorconfig(?1, ?2) AS a(b integer) ", nativeQuery = true)
    List<Object> getVendorConfigApplication(String requestId, String referenceId);

}

