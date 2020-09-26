package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.AllocationHistoryView;



public interface AllocationHistoryViewDao extends JpaRepository<AllocationHistoryView, Long>, JpaSpecificationExecutor {
    @Query("SELECT A FROM AllocationHistoryView A " +
            "JOIN UserDetail B " +
            "    ON A.teamName = B.teamName " +
            "    AND B.userRole = 'role_supervisor' " +
            "    AND B.userName = :userName " +
            "    AND B.teamName = :teamName")
    Page<AllocationHistoryView> findAllRoleSup(@Param("userName") String userName, @Param("teamName") String teamName, Pageable pageable);
}

