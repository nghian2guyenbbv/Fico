package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.UserDetail;

import org.springframework.data.domain.Pageable;

public interface UserDetailsDAO extends JpaRepository<UserDetail, Long> {

    @Query("SELECT e FROM UserDetail e WHERE e.teamName LIKE :teamName AND e.userRole = 'role_user'")
    Page<UserDetail> findAllUserForLeader(@Param("teamName") String teamName, Pageable pageable);

    @Query("SELECT e FROM UserDetail e WHERE e.teamName LIKE :teamName AND e.userRole = 'role_user' AND e.userRole = 'role_admin' ")
    Page<UserDetail> findAllUserForSub(@Param("teamName") String teamName, Pageable pageable);
}
