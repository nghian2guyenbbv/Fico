package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.AssignmentDetail;
import vn.com.tpf.microservices.models.TeamConfig;

import java.sql.Timestamp;
import java.util.List;

public interface AssignmentDetailDAO extends JpaRepository<AssignmentDetail, Long> {

    @Query(value= "SELECT e FROM AssignmentDetail e WHERE e.statusAssign = :statusAssign")
    Page<AssignmentDetail> findByStatusAssign(@Param("statusAssign") String statusAssign, Pageable pageable);

    AssignmentDetail findAssignmentDetailByAppNumberAndAssigneeAndStatusAssign(String appNumber, String assignee, String statusAssign);

    List<AssignmentDetail> findByAppNumberAndStageNameAndAndCreationApplStageTime(@Param("appNumber") String appNumber, @Param("stageName") String stageName, @Param("creationApplStageTime") Timestamp creationApplStageTime);

    AssignmentDetail findByAppNumberAndStageName(@Param("appNumber") String appNumber, @Param("stageName") String stageName);
}
